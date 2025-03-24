package ru.practicum.ewm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.client.StatisticClient;

@RestController
@RequiredArgsConstructor
public class MainController {
    private final StatisticClient statisticClient;
}