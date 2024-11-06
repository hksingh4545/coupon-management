package com.e_commerce.coupon_management.model;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;

public class CouponDTODeserializer extends JsonDeserializer<CouponDTO> {
    @Override
    public CouponDTO deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode root = mapper.readTree(p);

        String type = root.get("type").asText();
        JsonNode detailsNode = root.get("details");

        return new CouponDTO(type,new ObjectMapper().writeValueAsString(detailsNode));
    }
}
