package com.doglegs.core.download;


import com.doglegs.core.rx.RxBus;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/8/22 15:43
 * @describe :下载文件
 */

public class FileResponseBody extends ResponseBody {

    private Response originalResponse;

    public FileResponseBody(Response originalResponse) {
        this.originalResponse = originalResponse;
    }

    @Override
    public MediaType contentType() {
        return originalResponse.body().contentType();
    }

    @Override
    public long contentLength() {// 返回文件的总长度，也就是进度条的max
        return originalResponse.body().contentLength();
    }

    @Override
    public BufferedSource source() {
        return Okio.buffer(new ForwardingSource(originalResponse.body().source()) {
            long bytesReaded = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                bytesReaded += bytesRead == -1 ? 0 : bytesRead;
                RxBus.getInstance().post(new DownloadBean(contentLength(), bytesReaded));
                // 通过RxBus发布进度信息
                return bytesRead;
            }
        });
    }
}
