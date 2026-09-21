package ru.embedding_model.ai_search.service;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@AllArgsConstructor
public class IngestionService {

    /*
    Три подвоха для твоего сетапа
    1. Хвост среза выбрасывается. Когда сплиттер срезает окно назад к пунктуации,
    кусок текста между срезом и концом окна не переносится в следующий чанк — он теряется.
    Это известная особенность реализации. Проверь сам, руками: сложи суммарную длину всех чанков и сравни с длиной исходного текста.
    Если второе больше — ты только что нашёл потерю данных у себя в пайплайне.
    2. Overlap'а нет вообще. Предложение на границе окон режется пополам, и вторая половина живёт в следующем чанке без контекста.
    Срез к пунктуации смягчает, но не решает. У TokenTextSplitter параметра overlap не существует — это не LangChain4j.
    3. Кириллица + чужой токенайзер. CL100K_base на русском тратит ~1 токен на 2-3 символа (на английском — ~4 символа/токен).
    Твои 500 токенов — это ~1000-1500 русских символов. И фильтр minChunkLengthToEmbed=100 на русском срабатывает при ~250-300 символах.

    Что бы я поменял в твоих настройках
    .withChunkSize(500)              // ок, но попробуй 200-300 — для точных фактов часто лучше
    .withMinChunkSizeChars(50)       // ок
    .withMinChunkLengthToEmbed(20)   // 100 — слишком агрессивно, буду резать короткие легитимные чанки
    .withMaxNumChunks(10000)         // 100 — мина под большие документы

    И главное: у тебя теперь есть измерительный инструмент — golden dataset.
    Сменил chunkSize → прогнал 5 вопросов → цифра вместо ощущений.
    Вот так и сравнивают стратегии чанкинга на работе, а не «мне кажется стало лучше».
     */

    // Алгоритм внутри TokenTextSplitter
    // 1. Текст → токены (jtokkit, кодировка CL100K_BASE — токенайзер OpenAI)
    //2. Если токенов ≤ chunkSize → вернуть одним чанком
    //3. Иначе → нарезать окна по chunkSize токенов
    //4. В каждом окне: срез назад до последнего знака препинания,
    //   но не дальше minChunkSizeChars символов от начала
    //5. Фильтр: чанки короче minChunkLengthToEmbed токенов → ВЫБРОСИТЬ
    //6. Лимит: больше maxNumChunks чанков → ОТБРОСИТЬ всё сверх
    private final TextSplitter textSplitter = TokenTextSplitter.builder()
                                                               //Целевой размер окна в токенах.
                                                               //Окно потом может стать короче за счёт среза к пунктуации

                                                               //Больше → «размытые» векторы, поиск неточный.
                                                               //Меньше → смысл рвётся, факт из двух предложений попадает в разные чанки
                                                               .withChunkSize(150)                // размер чанка в токенах

                                                               //Не «мин. размер чанка», а граница среза: срезаем назад к последней пунктуации,
                                                               //только если она стоит после 50-го символа. Иначе окно остаётся целиком

                                                               //Мало → появляются огрызки из пары слов.
                                                               //Много → срез почти никогда не срабатывает, режем посреди предложений
                                                               .withMinChunkSizeChars(100)        // мин размер в символах

                                                               //Чанки короче 100 токенов молча выбрасываются — не эмбеддятся, не сохраняются

                                                               //Это тот самый тихий убийца из моего кейса с 80 токенами
                                                               .withMinChunkLengthToEmbed(20)     // мин длина для embedding

                                                               //Жёсткий потолок чанков на один документ. Всё сверх — молча в мусор

                                                               //Залишь PDF на 300 чанков → сохранились первые 100, остальные потеряны,
                                                               //и никто тебе об этом не скажет
                                                               .withMaxNumChunks(100)             // макс количество чанков

                                                               //Чем считать токены. По умолчанию — токенайзер OpenAI (GPT-4), не твой bge-m3

                                                               //Счёт приблизительный: свой токенайзер модели насчитал бы иначе
                                                               //.withEncodingType(EncodingType.CL100K_BASE)

                                                               //Какие знаки считать «точками среза»

                                                               //Мало знаков → режет только по точкам
                                                               //.withPunctuationMarks()

                                                               //Оставлять ли разделители на границах

                                                               //Для token-сплиттера почти не влияет, декоративный
                                                               .withKeepSeparator(true)           // сохранять разделители
                                                               .build();

    // minChunkLengthToEmbed=100 — чанки короче 100 токенов отбрасываются, не эмбеддятся, не попадают в ОС.
    // Твой текст в 80 токенов: сплиттер вернёт его одним чанком (меньше chunkSize), но на фильтре minChunkLengthToEmbed он умрёт.
    // В OpenSearch ничего не попадёт, /doc отдаст 200 OK, и ты узнаешь об этом только когда поиск вернёт пустоту.
    private final VectorStore vectorStore;

    public int ingest(final String text) {
        final Document document = new Document(text);
        final List<Document> chunks = textSplitter.apply(List.of(document));
        if (CollectionUtils.isEmpty(chunks)) {
            throw new IllegalArgumentException("text produced no chunks (too short or below minChunkLengthToEmbed)");
        }
        vectorStore.add(chunks);
        return chunks.size();
    }

}
