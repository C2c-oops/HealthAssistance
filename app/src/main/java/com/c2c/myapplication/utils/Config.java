package com.c2c.myapplication.utils;

public class Config {

    public static final String DATA_URL = "https://api.covid19india.org/data.json";
    public static final String DATA_URLT = "https://api.rootnet.in/covid19-in/stats/testing/latest";


    public static final String TAG_STATE = "state";

    public static final String TAG_ACTIVE = "active";
    public static final String TAG_CONFIRMED = "confirmed";
    public static final String TAG_DEATHS = "deaths";
    public static final String TAG_RECOVERED = "recovered";
    public static final String TAG_DATETIME = "lastupdatedtime";
    public static final String TAG_DCONFIRMED = "deltaconfirmed";
    public static final String TAG_DDEATHS = "deltadeaths";
    public static final String TAG_DRECOVERED = "deltarecovered";

    public static final String TAG_TESTED = "totalSamplesTested";

    public static final String JSON_ARRAY = "statewise";
    public static final String JSON_TOBJECT = "data";

}

/*JSON Attributes
    {
			"active": "7890",
			"confirmed": "9915",
			"deaths": "432",
			"deltaconfirmed": "0",
			"deltadeaths": "0",
			"deltarecovered": "0",
			"lastupdatedtime": "29/04/2020 22:37:46",
			"recovered": "1593",
			"state": "Maharashtra",
			"statecode": "MH",
			"statenotes": ""
		}*/

/*"statewise": [
		{
			"active": "24242",
			"confirmed": "34149",
			"deaths": "1120",
			"deltaconfirmed": "1086",
			"deltadeaths": "41",
			"deltarecovered": "347",
			"lastupdatedtime": "30/04/2020 21:01:45",
			"recovered": "8784",
			"state": "Total",
			"statecode": "TT",
			"statenotes": ""
		}*/

/*{
  "success": true,
  "data": {
    "day": "2020-04-30",
    "totalSamplesTested": 830201,
    "totalIndividualsTested": null,
    "totalPositiveCases": null,
    "source": "https://www.icmr.gov.in/pdf/covid/update/ICMR_testing_update_30Apr2020_9AM_IST.pdf"
  },
  "lastRefreshed": "2020-04-30T12:20:01.900Z",
  "lastOriginUpdate": "2020-04-30T03:30:00.000Z"
}*/
