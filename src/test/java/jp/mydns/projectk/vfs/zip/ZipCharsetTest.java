/*
 * Copyright (c) 2023, Project-K
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package jp.mydns.projectk.vfs.zip;

import jakarta.json.Json;
import jakarta.json.JsonValue;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import org.junit.jupiter.api.Test;
import test.ZipConfigUtils;

/**
 * Test of class ZipCharset.
 *
 * @author riru
 * @version 1.0.0
 * @since 1.0.0
 */
class ZipCharsetTest {

    /**
     * Test constructor. If argument is valid {@code JsonValue}.
     *
     * @since 1.0.0
     */
    @Test
    void testConstructor_JsonValue() {

        assertThat(new ZipCharset(Json.createValue("US-ASCII")).getValue()).isEqualTo(Json.createValue("US-ASCII"));

    }

    /**
     * Test constructor. If argument is illegal {@code JsonValue}.
     *
     * @since 1.0.0
     */
    @Test
    void testConstructor_IllegalJsonValue() {

        assertThatIllegalArgumentException().isThrownBy(() -> new ZipCharset(JsonValue.NULL))
                .withMessage("FileOption value of [%s] must be convertible to charset.", "zip:charset");

    }

    /**
     * Test constructor. If argument is valid {@code Charset}.
     *
     * @since 1.0.0
     */
    @Test
    void testConstructor_Charset() {

        assertThat(new ZipCharset(StandardCharsets.UTF_8).getValue()).isEqualTo(Json.createValue("UTF-8"));

    }

    /**
     * Test apply method.
     *
     * @since 1.0.0
     */
    @Test
    void testApply() throws FileSystemException {

        FileSystemOptions opts = new FileSystemOptions();

        new ZipCharset.Resolver().newInstance(Json.createValue("US-ASCII")).apply(opts);

        assertThat(extractValue(opts)).isEqualTo(StandardCharsets.US_ASCII);

        new ZipCharset.Resolver().newInstance(Json.createValue("UTF-8")).apply(opts);

        assertThat(extractValue(opts)).isEqualTo(StandardCharsets.UTF_8);

    }

    /**
     * Test {@code equals} method and {@code hashCode} method.
     *
     * @since 1.0.0
     */
    @Test
    void testEqualsHashCode() {

        ZipCharset base = new ZipCharset(StandardCharsets.UTF_16BE);
        ZipCharset same = new ZipCharset(StandardCharsets.UTF_16BE);
        ZipCharset another = new ZipCharset(StandardCharsets.UTF_16LE);

        assertThat(base).hasSameHashCodeAs(same).isEqualTo(same)
                .doesNotHaveSameHashCodeAs(another).isNotEqualTo(another);

    }

    /**
     * Test of toString method.
     *
     * @since 1.0.0
     */
    @Test
    void testToString() {

        var result = new ZipCharset(StandardCharsets.ISO_8859_1).toString();

        assertThat(result).isEqualTo("{\"zip:charset\":\"ISO-8859-1\"}");

    }

    Charset extractValue(FileSystemOptions opts) {

        return new ZipConfigUtils().getParam(opts, ".charset");

    }
}
