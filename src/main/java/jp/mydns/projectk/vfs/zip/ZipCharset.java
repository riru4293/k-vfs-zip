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
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Objects;
import java.util.ServiceLoader;
import jp.mydns.projectk.vfs.AbstractFileOption;
import jp.mydns.projectk.vfs.FileOption;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.zip.ZipFileSystemConfigBuilder;

/**
 * The charactar set.
 * <p>
 * Implementation requirements.
 * <ul>
 * <li>This class is immutable and thread-safe.</li>
 * <li>This class and JSON can be converted bidirectionally.</li>
 * <li>Can reflect this class on the {@link FileSystemOptions}.</li>
 * </ul>
 *
 * @author riru
 * @version 1.0.0
 * @since 1.0.0
 * @see ZipFileSystemConfigBuilder#getCharset(org.apache.commons.vfs2.FileSystemOptions)
 */
@FileOption.Name("zip:charset")
public class ZipCharset extends AbstractFileOption {

    private final Charset value;

    /**
     * Constructor.
     *
     * @param value option value
     * @throws NullPointerException if {@code value} is {@code null}
     * @throws IllegalArgumentException if {@code value} is not convertible to type {@link Charset}
     * @since 1.0.0
     */
    public ZipCharset(JsonValue value) {

        Objects.requireNonNull(value);

        try {

            this.value = Charset.forName(JsonString.class.cast(value).getString());

        } catch (ClassCastException | IllegalCharsetNameException | UnsupportedCharsetException ex) {

            throw new IllegalArgumentException(
                    "FileOption value of [zip:charset] must be convertible to charset.");

        }

    }

    /**
     * Constructor.
     *
     * @param value the {@code Charset}
     * @throws NullPointerException if {@code value} is {@code null}
     * @since 1.0.0
     */
    protected ZipCharset(Charset value) {

        this.value = Objects.requireNonNull(value);

    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    public JsonValue getValue() {

        return Json.createValue(value.name());

    }

    /**
     * {@inheritDoc}
     *
     * @since 1.0.0
     */
    @Override
    public void apply(FileSystemOptions opts) {

        Objects.requireNonNull(opts);

        ZipFileSystemConfigBuilder.getInstance().setCharset(opts, value);

    }

    /**
     * Returns a hash code value.
     *
     * @return a hash code value
     * @since 1.0.0
     */
    @Override
    public int hashCode() {

        return Objects.hash(getName(), getValue());

    }

    /**
     * Indicates that other object is equal to this one.
     *
     * @param other an any object
     * @return {@code true} if equals otherwise {@code false}
     * @since 1.0.0
     */
    @Override
    public boolean equals(Object other) {

        return other instanceof ZipCharset o
                && Objects.equals(getName(), o.getName())
                && Objects.equals(getValue(), o.getValue());

    }

    /**
     * Returns a string representation of this.
     *
     * @return string representation
     * @since 1.0.0
     */
    @Override
    public String toString() {

        return Json.createObjectBuilder().add(getName(), getValue()).build().toString();

    }

    /**
     * Resolver for {@link ZipCharset} instance from JSON.
     * <p>
     * Implementation requirements.
     * <ul>
     * <li>This class is immutable and thread-safe.</li>
     * <li>Implementations of this interface must be able to construct instances using {@link ServiceLoader}.</li>
     * <li>This class must be able to construct an instance of {@code FileOption} from the JSON representing
     * {@code FileOption}.</li>
     * </ul>
     *
     * @author riru
     * @version 1.0.0
     * @since 1.0.0
     */
    public static class Resolver implements FileOption.Resolver {
    }
}
