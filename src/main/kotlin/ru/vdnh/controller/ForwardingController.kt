package ru.vdnh.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

/**
 * Пользователь может напрямую вбивать в адресную строку пути и будет получать 404 ошибку от веб сервера,
 * так как существует только index.html, остальные страницы реализованы на react.
 * Чтобы уйти от этой проблемы, добавлен контроллер, перенаправляющий все запросы к корневой странице,
 * а далее маршрутизирует react router.
 * Запросы к бэкэнд контроллерам не перенаправляются.
 */
@Controller
class ForwardingController {

    @GetMapping("/{path:[^\\\\.]*}")
    fun forward(): String {
        return "forward:/"
    }
}
