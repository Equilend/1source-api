package com.os.console.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.os.client.model.CurrencyCd;
import com.os.client.model.Instrument;
import com.os.client.model.Price;
import com.os.client.model.PriceUnit;

public class InstrumentUtil {

	private static final Logger logger = LoggerFactory.getLogger(InstrumentUtil.class);

	Random r = new Random();
	int cnt = 1;

	private String instrumentsFile = "/instruments.csv";

	private InstrumentUtil() {

		BufferedReader reader = null;

		try {

			InputStream in = this.getClass().getResourceAsStream(instrumentsFile);

			reader = new BufferedReader(new InputStreamReader(in));

			String nextLine;
			while ((nextLine = reader.readLine()) != null) {

				String[] lineParts = nextLine.split(",");

				Instrument instrument = createInstrument(lineParts);
				instrumentList.add(instrument);

				instrumentMap.put(instrument.getIsin(), instrument);
				instrumentMap.put(instrument.getSedol(), instrument);
				instrumentMap.put(instrument.getCusip(), instrument);
				instrumentMap.put(instrument.getTicker(), instrument);
				instrumentMap.put(instrument.getFigi(), instrument);

				cnt++;
			}
		} catch (Exception e) {
			logger.error("Trouble loading instruments.", e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static InstrumentUtil _instrumentUtil;

	public static InstrumentUtil getInstance() {

		if (_instrumentUtil == null) {
			_instrumentUtil = new InstrumentUtil();
		}

		return _instrumentUtil;
	}

	List<Instrument> instrumentList = new ArrayList<>();
	Map<String, Instrument> instrumentMap = new HashMap<>();

	public Instrument getRandomInstrument() {
		return instrumentList.get(r.nextInt(cnt));
	}

	public Instrument getInstrument(String securityId) {
		return instrumentMap.get(securityId);
	}

	private Instrument createInstrument(String[] lineParts) {

		Instrument instrument = new Instrument();

		int idx = 0;
		instrument.setFigi(parseString(lineParts[idx++]));
		instrument.setIsin(parseString(lineParts[idx++]));
		instrument.setCusip(parseString(lineParts[idx++]));
		instrument.setSedol(parseString(lineParts[idx++]));
		instrument.setTicker(parseString(lineParts[idx++]));
		instrument.setMarketCd(parseString(lineParts[idx++]));
		instrument.setDescription(parseString(lineParts[idx++]));

		Price price = new Price();
		price.setValue(parseDouble(lineParts[idx++]));
		price.setCurrency(CurrencyCd.valueOf(parseString(lineParts[idx++])));
		price.setValueDate(LocalDate.now());
		price.setUnit(PriceUnit.SHARE);
		instrument.setPrice(price);

		return instrument;
	}

	public CurrencyCd getInstrumentCurrency(String marketCd) {

		CurrencyCd currencyCd = CurrencyCd.USD;

		switch (marketCd) {
		case "HK":
			currencyCd = CurrencyCd.HKD;
			break;
		case "JP":
			currencyCd = CurrencyCd.JPY;
			break;
		case "NO":
			currencyCd = CurrencyCd.NOK;
			break;
		case "SE":
			currencyCd = CurrencyCd.SEK;
			break;
		case "SG":
			currencyCd = CurrencyCd.SGD;
			break;
		case "AU":
			currencyCd = CurrencyCd.AUD;
			break;
		case "CA":
			currencyCd = CurrencyCd.CAD;
			break;
		case "CH":
			currencyCd = CurrencyCd.CHF;
			break;
		case "DK":
			currencyCd = CurrencyCd.DKK;
			break;
		case "EU":
			currencyCd = CurrencyCd.EUR;
			break;
		case "GB":
			currencyCd = CurrencyCd.GBP;
			break;
		}

		return currencyCd;
	}

	private String parseString(String s) {
		if (s == null || s.trim().length() == 0) {
			return null;
		}

		s = s.replace("\"", "");

		return s;
	}

	private Double parseDouble(String s) {
		if (s == null || s.trim().length() == 0) {
			return null;
		}

		return Double.valueOf(s);
	}

}
