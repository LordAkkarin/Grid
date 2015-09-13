/*
 * Copyright 2015 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package rocks.spud.grid.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import rocks.spud.grid.api.ErrorCode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

/**
 * Provides a simple interface for reading and/or writing data from/into {@link io.netty.buffer.ByteBuf} instances.
 *
 * @author Johannes Donath
 */
public class Packet {
        public static final Charset CHARSET = StandardCharsets.UTF_8;

        private final ByteBuf buffer;

        private Packet (@Nonnull ByteBuf buffer) {
                this.buffer = buffer;
        }

        /**
         * Retrieves a byte array representation.
         *
         * @return The array.
         */
        @Nonnull
        public byte[] array () {
                byte[] array = new byte[this.buffer ().readableBytes ()];
                this.buffer ().readBytes (array);
                return array;
        }

        /**
         * Retrieves the wrapped {@link io.netty.buffer.ByteBuf} instance.
         *
         * @return The {@link io.netty.buffer.ByteBuf}.
         */
        @Nonnull
        public ByteBuf buffer () {
                return this.buffer;
        }

        /**
         * Creates an empty wrapped {@link io.netty.buffer.ByteBuf} instance.
         *
         * @return The empty {@link rocks.spud.grid.util.Packet}.
         */
        public static Packet empty () {
                return (new Packet (Unpooled.buffer ()));
        }

        /**
         * Reads a set of {@link rocks.spud.grid.api.ErrorCode}s.
         *
         * @return The {@link rocks.spud.grid.api.ErrorCode} set.
         */
        @Nonnull
        public Set<ErrorCode> read () {
                return ErrorCode.of (readInteger ());
        }

        /**
         * Reads an array of {@link java.lang.Boolean} values.
         *
         * @return The array.
         */
        @Nullable
        public boolean[] readBooleanArray () {
                int length = this.buffer ().readUnsignedShort ();
                if (length == 0) { return null; }

                boolean[] array = new boolean[length];
                byte[] bytes = new byte[((length / 8) + (length % 8 != 0 ? 1 : 0))];
                this.buffer ().readBytes (bytes);

                for (int i = 0; i < length; i++) {
                        int index = (i / 8);
                        int offset = (i % 8);

                        array[i] = (((bytes[index] >> offset) & 0x1) == 0x1);
                }

                return array;
        }

        /**
         * Reads a {@link java.lang.Byte}.
         *
         * @return The {@link java.lang.Byte}.
         */
        public byte readByte () {
                return this.buffer ().readByte ();
        }

        /**
         * Reads an array of {@link java.lang.Byte}s.
         *
         * @return The {@link java.lang.Byte} array.
         */
        @Nonnull
        public byte[] readBytes () {
                int length = this.buffer ().readUnsignedShort ();
                if (length == 0) { return new byte[0]; }

                byte[] bytes = new byte[length];
                buffer.readBytes (bytes, 0, length);

                return bytes;
        }

        /**
         * Reads a set of {@link rocks.spud.grid.api.ErrorCode}s.
         *
         * @return The {@link rocks.spud.grid.api.ErrorCode} set.
         */
        @Nonnull
        public Set<ErrorCode> readErrorCode () {
                return ErrorCode.of (this.readInteger ());
        }

        /**
         * Reads an {@link java.lang.Integer}.
         *
         * @return The {@link java.lang.Integer}.
         */
        public int readInteger () {
                return this.buffer ().readInt ();
        }

        /**
         * Reads a {@link java.lang.Long}.
         *
         * @return The {@link java.lang.Long}.
         */
        public long readLong () {
                return this.buffer ().readLong ();
        }

        /**
         * Reads a {@link java.lang.Short}.
         *
         * @return The {@link java.lang.Short}.
         */
        public short readShort () {
                return this.buffer ().readShort ();
        }

        /**
         * Reads a {@link java.lang.String}.
         *
         * @return The {@link java.lang.String}.
         */
        @Nonnull
        public String readString () {
                return (new String (readBytes (), CHARSET));
        }

        /**
         * Reads a {@link java.util.UUID}.
         *
         * @return The {@link java.util.UUID}.
         */
        @Nonnull
        public UUID readUUID () {
                return (new UUID (this.readLong (), this.readLong ()));
        }

        /**
         * Wraps a {@link io.netty.buffer.ByteBuf} instance.
         *
         * @param buffer The buffer.
         * @return The {@link Packet} instance.
         */
        @Nonnull
        public static Packet wrap (@Nonnull ByteBuf buffer) {
                return (new Packet (buffer));
        }

        /**
         * Wraps an array of {@link java.lang.Byte}s.
         *
         * @param data The {@link java.lang.Byte} array.
         * @return The {@link Packet} instance.
         */
        @Nonnull
        public static Packet wrap (@Nonnull byte[] data) {
                return wrap (Unpooled.wrappedBuffer (data));
        }

        /**
         * Writes a {@link java.util.UUID}.
         *
         * @param value The {@link java.util.UUID}.
         * @return The {@link rocks.spud.grid.util.Packet} instance.
         */
        @Nonnull
        public Packet write (@Nonnull UUID value) {
                this.buffer ().writeLong (value.getMostSignificantBits ());
                this.buffer ().writeLong (value.getLeastSignificantBits ());
                return this;
        }

        /**
         * Writes a {@link java.lang.Long}.
         *
         * @param value The {@link java.lang.Long}.
         * @return The {@link rocks.spud.grid.util.Packet} instance.
         */
        @Nonnull
        public Packet write (long value) {
                this.buffer ().writeLong (value);
                return this;
        }

        /**
         * Writes a {@link java.lang.Byte}.
         *
         * @param value The {@link java.lang.Byte}.
         * @return The {@link rocks.spud.grid.util.Packet} instance.
         */
        @Nonnull
        public Packet write (byte value) {
                this.buffer ().writeByte (value);
                return this;
        }

        /**
         * Writes an array of {@link java.lang.Boolean}s.
         *
         * @param array The {@link java.lang.Boolean} array.
         * @return The {@link rocks.spud.grid.util.Packet} instance.
         */
        @Nonnull
        public Packet write (@Nullable boolean[] array) {
                if (array == null || array.length == 0) {
                        this.buffer ().writeShort (0);
                        return this;
                }

                byte[] bytes = new byte[((array.length / 8) + (array.length % 8 != 0 ? 1 : 0))];

                for (int i = 0; i < array.length; i++) {
                        if (!array[i]) { continue; }

                        int index = (i / 8);
                        int offset = (i % 8);

                        bytes[index] |= (0x1 << offset);
                }

                this.buffer ().writeShort (array.length);
                this.buffer ().writeBytes (bytes);

                return this;
        }

        /**
         * Writes a {@link java.lang.Boolean}.
         *
         * @param value The {@link java.lang.Boolean}.
         * @return The {@link rocks.spud.grid.util.Packet} instance.
         */
        @Nonnull
        public Packet write (boolean value) {
                this.buffer ().writeBoolean (value);
                return this;
        }

        /**
         * Writes a {@link java.lang.Short}.
         *
         * @param value The {@link java.lang.Short}.
         * @return The {@link rocks.spud.grid.util.Packet} instance.
         */
        @Nonnull
        public Packet write (short value) {
                this.buffer ().writeShort (value);
                return this;
        }

        /**
         * Writes an array of {@link java.lang.Byte}s.
         *
         * @param array The {@link java.lang.Byte} array.
         * @return The {@link Packet} instance.
         */
        @Nonnull
        public Packet write (@Nullable byte[] array) {
                if (array == null || array.length == 0) {
                        this.buffer ().writeShort (0);
                        return this;
                }

                this.buffer.writeShort (array.length);
                this.buffer.writeBytes (array);

                return this;
        }

        /**
         * Writes a {@link java.lang.Integer}.
         *
         * @param value The {@link java.lang.Integer}.
         * @return The {@link rocks.spud.grid.util.Packet} instance.
         */
        @Nonnull
        public Packet write (int value) {
                this.buffer ().writeInt (value);
                return this;
        }

        /**
         * Writes a {@link java.lang.String}.
         *
         * @param value The {@link java.lang.String}.
         * @return The {@link Packet} instance.
         */
        @Nonnull
        public Packet write (@Nullable String value) {
                if (value == null) { return write (((byte[]) null)); }
                return write (value.getBytes (CHARSET));
        }

        /**
         * Writes a set of {@link rocks.spud.grid.api.ErrorCode}s.
         *
         * @param codes The {@link rocks.spud.grid.api.ErrorCode} set.
         * @return The {@link rocks.spud.grid.util.Packet} instance.
         */
        @Nonnull
        public Packet write (@Nonnull ErrorCode... codes) {
                return write (ErrorCode.of (codes));
        }
}
