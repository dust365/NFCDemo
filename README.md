# NFCDemo

说明 ：关于Android NfC的一个demo，目前仅仅支持部分卡类型的读取，后续会逐步进行完善

                 if (TextUtils.equals(aTechList, "android.nfc.tech.NdefFormatable")) {
                     cardType += "NdefFormatable" + "\n";
                     cardType += "----------------------------------------" + "\n";
 
 
                 } else if (TextUtils.equals(aTechList, "android.nfc.tech.MifareClassic")) {
 
                     cardType += "MifareClassic" + "\n";
                     cardType += "----------------------------------------" + "\n";
                     cardType = MifareClassiceRead(tag, cardType);
 
 
                 } else if (TextUtils.equals(aTechList, "android.nfc.tech.MifareUltralight")) {
                     cardType += "MifareUltralight" + "\n";
                     cardType += "----------------------------------------" + "\n";
 
                 } else if (TextUtils.equals(aTechList, "android.nfc.tech.IsoDep")) {
 
                     cardType += "IsoDep" + "\n";
                     cardType += "----------------------------------------" + "\n";
                     IsoDep isoDep = IsoDep.get(tag);
                     String isoDeps = isoDep.toString();
                     Log.e(TAG, "isoDeps==" + isoDeps);

 
                 }

##参考文献：  
1. 关于NFC 卡类型的了解  国外的一个网站 https://developer.xamarin.com/api/type/Android.Nfc.Tech.MifareClassic/
            
2. 关于MifareClassic卡（读写）  http://www.cnitblog.com/asfman/articles/86871.html
            
3. Android官网关于NFc https://developer.android.com/guide/topics/connectivity/nfc/index.html
