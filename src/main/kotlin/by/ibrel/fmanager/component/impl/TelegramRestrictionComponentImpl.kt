package by.ibrel.fmanager.component.impl

import by.ibrel.fmanager.component.RestrictionComponent
import by.ibrel.fmanager.model.LoginRequest
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.text
import com.github.kotlintelegrambot.entities.ChatId
import org.springframework.stereotype.Component

@Component
class TelegramRestrictionComponentImpl : RestrictionComponent {

    override fun isAllow(loginRequest: LoginRequest) : Boolean {

        val bot = bot {
            token = "968054265:AAGcja7jLrEbDJ86Hfns30Ex8GjbY4xtr7w"
            dispatch {
                text {
//                    val text = update.message?.text ?: "Hello, World!"
                    bot.sendMessage(ChatId.fromId(message.chat.id), text = "text")
                }
            }
        }

        bot.startPolling()

        val chat = bot.getChat(ChatId.fromChannelUsername("ibrel")).get()

        return true
    }
}