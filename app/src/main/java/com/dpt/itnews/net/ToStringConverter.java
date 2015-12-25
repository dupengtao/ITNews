package com.dpt.itnews.net;

import com.squareup.okhttp.ResponseBody;
import retrofit.Converter;

import java.io.IOException;

public class ToStringConverter implements Converter<ResponseBody,String> {
    @Override
    public String convert(ResponseBody value) throws IOException {
        return new String(value.bytes(), "UTF-8");
    }
}