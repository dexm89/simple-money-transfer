package com.moneytransfer.transformer;

import com.moneytransfer.util.JsonUtils;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    @Override
    public String render(final Object model) {
        return JsonUtils.toJson(model);
    }
}
