package edu.technopolis.homework.messenger.messages;

import edu.technopolis.homework.messenger.store.datasets.User;

import java.util.Objects;

/**
 * Простое текстовое сообщение
 */
public class TextMessage extends Message {
    private String text;

    public TextMessage() {
    }

    public TextMessage(User sender, Long chatId, String text) {
        super(sender, chatId, Type.MSG_TEXT);
        this.text = text;
    }

    public TextMessage(Long senderId, Long chatId, String text) {
        super(senderId, chatId, Type.MSG_TEXT);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        TextMessage message = (TextMessage) other;
        return Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), text);
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "text='" + text + '\'' +
                '}' + "\n" + super.toString();
    }
}