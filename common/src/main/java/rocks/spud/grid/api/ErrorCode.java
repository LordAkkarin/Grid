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
package rocks.spud.grid.api;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Provides a list of known error codes within the network protocol.
 *
 * @author Johannes Donath
 */
public enum ErrorCode {
        // Generic Errors
        SUCCESS (0x0),          // => 0b0000000 => 0 => 0x0
        DUPLICATE (0x1),        // => 0b0000001 => 1 => 0x1
        // => 0b0000010 => 2 => 0x2
        // => 0b0000100 => 4 => 0x4
        // => 0b0001000 => 8 => 0x8
        // => 0b0010000 => 16 => 0x10
        // => 0b0100000 => 32 => 0x20
        // => 0b1000000 => 64 => 0x40

        // Registry Errors
        NO_SUCH_CHANNEL (0x80), // => 0b000000010000000 => 128 => 0x80
        NO_SUCH_PLAYER (0x100), // => 0b000000100000000 => 256 => 0x100
        // => 0b000001000000000 => 512 => 0x200
        // => 0b000010000000000 => 1024 => 0x400
        // => 0b000100000000000 => 2048 => 0x800
        // => 0b001000000000000 => 4096 => 0x1000
        // => 0b010000000000000 => 8192 => 0x2000
        // => 0b100000000000000 => 16258 => 0x4000

        // Channel Errors
        PERMANENT (0x800),              // => 0b00000001000000000000000 => 32516 => 0x8000
        NON_PERMANENT (0x10000),        // => 0b00000010000000000000000 => 32516 => 0x10000
        // => 0b00000100000000000000000 => 130064 => 0x20000
        // => 0b00001000000000000000000 => 260128 => 0x40000
        // => 0b00010000000000000000000 => 520256 => 0x80000
        // => 0b00100000000000000000000 => 1040512 => 0x100000
        // => 0b01000000000000000000000 => 2081024 => 0x200000
        // => 0b10000000000000000000000 => 4162048 => 0x400000

        // Subscription
        SUBSCRIBED (0x80000),           // => 0b0000000100000000000000000000000 => 8324096 => 0x80000
        NOT_SUBSCRIBED (0x100000);      // => 0b0000001000000000000000000000000 => 16648192 => 0x1000000
        // => 0b0000010000000000000000000000000 => 33296384 => 0x2000000
        // => 0b0000100000000000000000000000000 => 66592768 => 0x4000000
        // => 0b0001000000000000000000000000000 => 133185536 => 0x8000000
        // => 0b0010000000000000000000000000000 => 266371072 => 0x10000000
        // => 0b0100000000000000000000000000000 => 532742144 => 0x20000000
        // => 0b1000000000000000000000000000000 => 1065484288 => 0x40000000

        private final int mask;

        ErrorCode (@Nonnegative long mask) {
                this.mask = ((int) (mask & 0xFFFFFFFFL));
        }

        /**
         * Checks whether a mask contains one or more errors.
         *
         * @param errorMask The mask.
         * @return {@code true} if the mask contains one or more errors, {@code false} otherwise.
         */
        public static boolean hasError (int errorMask) {
                return (errorMask == ErrorCode.SUCCESS.mask);
        }

        /**
         * Checks whether a set contains one or more errors.
         *
         * @param errorCodes The set.
         * @return {@code true} if the mask contains one or more errors, {@code false} otherwise.
         */
        public static boolean hasError (@Nonnull Set<ErrorCode> errorCodes) {
                for (ErrorCode code : errorCodes) {
                        if (code != null && code != ErrorCode.SUCCESS) { return true; }
                }

                return false;
        }

        /**
         * Retrieves an error mask containing a set of specified codes.
         *
         * @param codes The list of {@link rocks.spud.grid.api.ErrorCode}s.
         * @return The error mask.
         */
        public static int of (@Nonnull ErrorCode... codes) {
                long value = 0x0;

                for (ErrorCode code : codes) {
                        if (code == null) { continue; }
                        value |= code.mask;
                }

                return ((int) (value & 0xFFFFFFFFL));
        }

        /**
         * Retrieves a set of error codes specified within a set.
         *
         * @param errorMask The mask.
         * @return The set.
         */
        @Nonnull
        public static Set<ErrorCode> of (int errorMask) {
                Set<ErrorCode> errorCodes = new HashSet<> ();

                for (ErrorCode code : values ()) {
                        if (code == ErrorCode.SUCCESS) { continue; }
                        if ((errorMask & code.mask) == code.mask) { errorCodes.add (code); }
                }

                return Collections.unmodifiableSet (errorCodes);
        }
}
