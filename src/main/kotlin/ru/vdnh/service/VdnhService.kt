package ru.vdnh.service

import org.springframework.stereotype.Service
import ru.vdnh.model.Message
import ru.vdnh.repository.VdnhRepository

@Service
class VdnhService(val db: VdnhRepository) {
    fun findMessages(): List<Message> = db.findMessages()

    fun post(message: Message) {
        db.save(message)
    }
}