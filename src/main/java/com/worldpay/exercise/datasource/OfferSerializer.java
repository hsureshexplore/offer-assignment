package com.worldpay.exercise.datasource;

import com.worldpay.exercise.domain.Offer;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/***
 * Custom serializer for Offer
 */
public class OfferSerializer implements Serializer<Offer>, Serializable {
    @Override
    public void serialize(@NotNull DataOutput2 out, @NotNull Offer value) throws IOException {
        out.writeUTF(value.getId());
        out.writeUTF(value.getDescription());
        out.writeUTF(value.getCurrency());
        out.writeUTF(value.getCreatedAt().toString());
        out.writeDouble(value.getPrice().doubleValue());
        out.writeInt(value.getValidityInSeconds());
        out.writeBoolean(value.isValid());
        out.writeUTF(value.toString());
    }

    @Override
    public Offer deserialize(@NotNull DataInput2 input, int available) throws IOException {
        String id = input.readUTF();
        String description = input.readUTF();
        String currency = input.readUTF();
        LocalDateTime createdAt =  LocalDateTime.parse(input.readUTF());
        BigDecimal price = new BigDecimal(input.readDouble());
        int validityInSeconds = input.readInt();
        boolean valid = input.readBoolean();
        return new Offer(id, description, price, currency, validityInSeconds, createdAt, valid);
    }
}
