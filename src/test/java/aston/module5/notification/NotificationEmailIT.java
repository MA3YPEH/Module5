package aston.module5.notification;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = NotificationServiceApp.class)
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = { "user-events" })
class NotificationEmailIT {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private KafkaTemplate<String, String> kafkaTemplate;

    @MockitoBean
    private JavaMailSender mailSender;

    @DynamicPropertySource
    static void overrideKafkaProperties(DynamicPropertyRegistry registry) {
        String brokers = System.getProperty("spring.kafka.embedded.kafka-brokers");
        registry.add("spring.kafka.bootstrap-servers", () -> brokers);
    }

    @BeforeEach
    void setUp() {
        Map<String, Object> producerProps = KafkaTestUtils.producerProps(embeddedKafkaBroker);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        DefaultKafkaProducerFactory<String, String> pf = new DefaultKafkaProducerFactory<>(producerProps);
        this.kafkaTemplate = new KafkaTemplate<>(pf);
    }

    @Test
    @DisplayName("IT Почта: Успешная отправка письма при событии CREATE")
    void testEmailSentOnCreateEvent() {
        kafkaTemplate.send("user-events", "CREATE:ivan@mail.ru");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        verify(mailSender, timeout(5000).times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage);
        assertEquals("ivan@mail.ru", sentMessage.getTo()[0]);
        assertEquals("Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.", sentMessage.getText());
    }

    @Test
    @DisplayName("IT Почта: Успешная отправка письма при событии DELETE")
    void testEmailSentOnDeleteEvent() {
        kafkaTemplate.send("user-events", "DELETE:ivan@mail.ru");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, timeout(5000).times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage);
        assertEquals("ivan@mail.ru", sentMessage.getTo()[0]);
        assertEquals("Здравствуйте! Ваш аккаунт был удалён.", sentMessage.getText());
    }

    @Test
    @DisplayName("IT Почта: Успешная отправка письма при событии UPDATE")
    void testEmailSentOnUpdateEvent() {
        kafkaTemplate.send("user-events", "UPDATE:ivan@mail.ru");

        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, timeout(5000).times(1)).send(messageCaptor.capture());

        SimpleMailMessage sentMessage = messageCaptor.getValue();
        assertNotNull(sentMessage);
        assertEquals("ivan@mail.ru", sentMessage.getTo()[0]);
        assertEquals("Здравствуйте! Ваш аккаунт был изменен.", sentMessage.getText());
    }
}