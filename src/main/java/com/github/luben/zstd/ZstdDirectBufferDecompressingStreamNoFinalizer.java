package com.github.luben.zstd;

import com.github.luben.zstd.util.Native;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ZstdDirectBufferDecompressingStreamNoFinalizer extends BaseZstdBufferDecompressingStreamNoFinalizer {
    static {
        Native.load();
    }

    public ZstdDirectBufferDecompressingStreamNoFinalizer(ByteBuffer source) {
        super(source);
        if (!source.isDirect()) {
            throw new IllegalArgumentException("Source buffer should be a direct buffer");
        }
        this.source = source;
        stream = createDStream();
        initDStream(stream);
    }

    @Override
    public int read(ByteBuffer target) throws IOException {
        if (!target.isDirect()) {
            throw new IllegalArgumentException("Target buffer should be a direct buffer");
        }
        return readInternal(target, true);
    }

    @Override
    MemoryAddress createDStream() {
        return createDStreamNative();
    }

    @Override
    long freeDStream(MemoryAddress stream) {
        return freeDStreamNative(stream);
    }

    @Override
    long initDStream(MemoryAddress stream) {
        return initDStreamNative(stream);
    }

    @Override
    long decompressStream(MemoryAddress stream, ByteBuffer dst, int dstOffset, int dstSize, ByteBuffer src, int srcOffset, int srcSize) {
        return decompressStreamNative(stream, dst, dstOffset, dstSize, src, srcOffset, srcSize);
    }

    public static int recommendedTargetBufferSize() {
        return (int) recommendedDOutSizeNative();
    }

    private static native MemoryAddress createDStreamNative();

    private static native long freeDStreamNative(MemoryAddress stream);

    private native long initDStreamNative(MemoryAddress stream);

    private native long decompressStreamNative(MemoryAddress stream, ByteBuffer dst, int dstOffset, int dstSize, ByteBuffer src, int srcOffset, int srcSize);

    private static native long recommendedDOutSizeNative();
}
