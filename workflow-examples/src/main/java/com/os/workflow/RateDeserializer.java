package com.os.workflow;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import io.swagger.v1_0_5_20240611.client.model.BenchmarkCd;
import io.swagger.v1_0_5_20240611.client.model.FeeRate;
import io.swagger.v1_0_5_20240611.client.model.FixedRate;
import io.swagger.v1_0_5_20240611.client.model.FixedRateDef;
import io.swagger.v1_0_5_20240611.client.model.FloatingRate;
import io.swagger.v1_0_5_20240611.client.model.FloatingRateDef;
import io.swagger.v1_0_5_20240611.client.model.Rate;
import io.swagger.v1_0_5_20240611.client.model.RebateRate;

public class RateDeserializer extends StdDeserializer<Rate> {

	private static final long serialVersionUID = 9024714383266337284L;

	public RateDeserializer() {
		this(null);
	}

	protected RateDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public Rate deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		
		Rate impl = null;
		
		JsonNode nodeRate = p.readValueAsTree();
		
		JsonNode nodeRateRebate = nodeRate.get("rebate");
		if (nodeRateRebate != null) {
			RebateRate rebateRate = new RebateRate();
			
			JsonNode nodeRateRebateFixed = nodeRateRebate.get("fixed");
			if (nodeRateRebateFixed != null) {
				FixedRate fixedRate = new FixedRate();
				rebateRate.setRebate(fixedRate);
				
				FixedRateDef fixedRateDef = new FixedRateDef();
				fixedRate.setFixed(fixedRateDef);
				
				JsonNode nodeBaseRate = nodeRateRebateFixed.get("baseRate");
				if (nodeBaseRate != null) {
					fixedRateDef.setBaseRate(nodeBaseRate.doubleValue());
				}

				JsonNode nodeEffectiveRate = nodeRateRebateFixed.get("effectiveRate");
				if (nodeEffectiveRate != null) {
					fixedRateDef.setEffectiveRate(nodeEffectiveRate.doubleValue());
				}

				JsonNode nodeEffectiveDate = nodeRateRebateFixed.get("effectiveDate");
				if (nodeEffectiveDate != null) {
					fixedRateDef.setEffectiveDate(Date.from(LocalDate.parse(nodeEffectiveDate.asText(), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneOffset.UTC).toInstant()));
				}

				JsonNode nodeCutoffTime = nodeRateRebateFixed.get("cutoffTime");
				if (nodeCutoffTime != null) {
					fixedRateDef.setCutoffTime(nodeCutoffTime.asText());
				}

			} else {
				JsonNode nodeRateRebateFloating = nodeRateRebate.get("floating");
				if (nodeRateRebateFloating != null) {
					FloatingRate floatingRate = new FloatingRate();
					rebateRate.setRebate(floatingRate);

					FloatingRateDef floatingRateDef = new FloatingRateDef();
					floatingRate.setFloating(floatingRateDef);
					
					JsonNode nodeBenchmark = nodeRateRebateFloating.get("benchmark");
					if (nodeBenchmark != null) {
						floatingRateDef.setBenchmark(BenchmarkCd.fromValue(nodeBenchmark.asText()));
					}
					
					JsonNode nodeBaseRate = nodeRateRebateFloating.get("baseRate");
					if (nodeBaseRate != null) {
						floatingRateDef.setBaseRate(nodeBaseRate.doubleValue());
					}

					JsonNode nodeSpread = nodeRateRebateFloating.get("spread");
					if (nodeSpread != null) {
						floatingRateDef.setSpread(nodeSpread.doubleValue());
					}

					JsonNode nodeIsAutoRerate = nodeRateRebateFloating.get("isAutoRerate");
					if (nodeIsAutoRerate != null) {
						floatingRateDef.setIsAutoRerate(nodeIsAutoRerate.asBoolean());
					}

					JsonNode nodeEffectiveRate = nodeRateRebateFloating.get("effectiveRate");
					if (nodeEffectiveRate != null) {
						floatingRateDef.setEffectiveRate(nodeEffectiveRate.doubleValue());
					}

					JsonNode nodeEffectiveDate = nodeRateRebateFloating.get("effectiveDate");
					if (nodeEffectiveDate != null) {
						floatingRateDef.setEffectiveDate(Date.from(LocalDate.parse(nodeEffectiveDate.asText(), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneOffset.UTC).toInstant()));
					}

					JsonNode nodeCutoffTime = nodeRateRebateFloating.get("cutoffTime");
					if (nodeCutoffTime != null) {
						floatingRateDef.setCutoffTime(nodeCutoffTime.asText());
					}
				}
			}
			
			impl = rebateRate;
		} else {
			JsonNode nodeRateFee = nodeRate.get("fee");
			if (nodeRateFee != null) {
				FeeRate feeRate = new FeeRate();
				impl = feeRate;
				
				FixedRateDef fixedRateDef = new FixedRateDef();
				feeRate.setFee(fixedRateDef);
				
				JsonNode nodeBaseRate = nodeRateFee.get("baseRate");
				if (nodeBaseRate != null) {
					fixedRateDef.setBaseRate(nodeBaseRate.doubleValue());
				}

				JsonNode nodeEffectiveRate = nodeRateFee.get("effectiveRate");
				if (nodeEffectiveRate != null) {
					fixedRateDef.setEffectiveRate(nodeEffectiveRate.doubleValue());
				}

				JsonNode nodeEffectiveDate = nodeRateFee.get("effectiveDate");
				if (nodeEffectiveDate != null) {
					fixedRateDef.setEffectiveDate(Date.from(LocalDate.parse(nodeEffectiveDate.asText(), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay(ZoneOffset.UTC).toInstant()));
				}

				JsonNode nodeCutoffTime = nodeRateFee.get("cutoffTime");
				if (nodeCutoffTime != null) {
					fixedRateDef.setCutoffTime(nodeCutoffTime.asText());
				}

			}
		}

		return impl;
	}
}
