package com.worldpay.exercise.datasource;

import com.worldpay.exercise.domain.Offer;
import org.jetbrains.annotations.NotNull;
import org.mapdb.DataInput2;
import org.mapdb.DataOutput2;
import org.mapdb.Serializer;
import org.mapdb.serializer.SerializerJava;

import java.io.*;

/***
 * Custom serializer for Offer
 */
public class OfferSerializer implements Serializer<Offer>, Serializable {
    private SerializerJava delegate = new SerializerJava();
    @Override
    public void serialize(@NotNull DataOutput2 out, @NotNull Offer value) throws IOException {
        delegate.serialize(out, value);
    }

    @Override
    public Offer deserialize(@NotNull DataInput2 in, int available) throws IOException {
        return (Offer) delegate.deserialize(in, available);
    }
}
