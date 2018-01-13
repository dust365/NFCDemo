package com.dy.nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

import static android.nfc.NdefRecord.createMime;

public class NfcScanActivity extends Activity implements NfcAdapter.CreateNdefMessageCallback{


    private static final String TAG = NfcScanActivity.class.getSimpleName();
    NfcAdapter mAdapter;
    TextView promt;
    NdefMessage mNdefPushMessage;
    PendingIntent mPendingIntent;
    String[][] techListsArray;
    private Button btnWrite;
    String datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_scan);
        promt = (TextView) findViewById(R.id.promt);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NfcScanActivity.this, NfcWriteTextActivity.class);
                if (datas != null)
                    intent.putExtra("data", datas);
                startActivity(intent);
            }
        });


        // 获取默认的NFC控制器
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            promt.setText("设备不支持NFC！");
            finish();
            return;
        }
        if (!mAdapter.isEnabled()) {
            promt.setText("请在系统设置中先启用NFC功能！");
            finish();
            return;
        }

        mAdapter.setNdefPushMessageCallback(this,this);
        mPendingIntent = PendingIntent
                .getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    public void onNewIntent(Intent paramIntent) {
        setIntent(paramIntent);
        resolveIntent(paramIntent);

//        if (paramIntent != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(paramIntent.getAction())) {
//
//            Tag tag = paramIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            // 获取标签id数组
//            byte[] bytesId = tag.getId();
//            Parcelable[] rawMessages = paramIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//            if (rawMessages != null) {
//                NdefMessage[] messages = new NdefMessage[rawMessages.length];
//                for (int i = 0; i < rawMessages.length; i++) {
//                    messages[i] = (NdefMessage) rawMessages[i];
//                }
//                // Process the messages array.
//
//            }
//        }else if (paramIntent != null && NfcAdapter.ACTION_TECH_DISCOVERED.equals(paramIntent.getAction())){
//
//            Tag tag = paramIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            // 获取标签id数组
//            byte[] bytesId = tag.getId();
//            Parcelable[] rawMessages = paramIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//            if (rawMessages != null) {
//                NdefMessage[] messages = new NdefMessage[rawMessages.length];
//                for (int i = 0; i < rawMessages.length; i++) {
//                    messages[i] = (NdefMessage) rawMessages[i];
//                }
//                // Process the messages array.
//
//            }
//
//
//        }else if (paramIntent != null && NfcAdapter.ACTION_TAG_DISCOVERED.equals(paramIntent.getAction())){
//
//            Tag tag = paramIntent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            Parcelable[] rawMessages = paramIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//
//            // 获取标签id数组
//            byte[] bytesId = tag.getId();
//            String results = tag.toString();
//            int i1 = tag.describeContents();
//
//            Log.e(TAG,"tag=="+tag+"i1=="+i1);
//
//
//            String info = bytesToHexString(bytesId);
//
//            if (rawMessages != null) {
//                NdefMessage[] messages = new NdefMessage[rawMessages.length];
//                for (int i = 0; i < rawMessages.length; i++) {
//                    messages[i] = (NdefMessage) rawMessages[i];
//                }
//                // Process the messages array.
//
//            }
//            promt.setText("NFC信息如下：\n" + info);
//
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.mAdapter == null)
            return;
        if (!this.mAdapter.isEnabled()) {
            promt.setText("请在系统设置中先启用NFC功能！");
        }
        this.mAdapter.enableForegroundDispatch(this, this.mPendingIntent, null, null);
    }


    protected void resolveIntent(Intent intent) {

        // 得到是否检测到TAG触发

           //此意图是用来启动一个活动时，它包含一个NDEF净荷标签的扫描和是一个公认的类型
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())



                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())


                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {






            // 处理该intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);




            // 获取标签id数组
            byte[] bytesId = tag.getId();

            //获取消息内容
            NfcMessageParser nfcMessageParser = new NfcMessageParser(intent);
            List<String> tagMessage = nfcMessageParser.getTagMessage();

            if (tagMessage == null || tagMessage.size() == 0) {

                //Toast.makeText(this, "NFC格式不支持...", Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < tagMessage.size(); i++) {
                    Log.e("tag", tagMessage.get(i));
                }
                datas = tagMessage.get(0);
            }
            String info = "";
            if (datas != null) {
                info += "内容：" + datas + "\n卡片ID：" + bytesToHexString(bytesId) + "\n";
            } else {
                info += "内容：空" + "\n卡片ID：" + bytesToHexString(bytesId) + "\n";
            }


            String[] techList = tag.getTechList();

            //分析NFC卡的类型： Mifare Classic/UltraLight Info
            String cardType = "";


            for (String aTechList : techList) {
                if (TextUtils.equals(aTechList, "android.nfc.tech.NfcA")) {

                    cardType += "NfcA" + "\n";

                    cardType += "----------------------------------------" + "\n";

                    NfcA nfcA = NfcA.get(tag);
                    String nfcAs = nfcA.toString();
                    Log.e(TAG, "nfcAs==" + nfcAs);


                    try {
                        nfcA.connect();

//                        byte[] SELECT = {(byte) 0x30, (byte) 0x05,};
                        byte[] SELECT = { (byte)0xC0, (byte)0x0E, (byte)0x00, (byte)0x00, (byte)0x00 };
                        Log.e(TAG, "SELECT="+bytesToHexString(SELECT));
                        byte[] result = nfcA.transceive(SELECT);


                        String resultstr = bytesToHexString(result);

                        Log.e(TAG, "写入后数据=="+resultstr);


//                        int data_len = ((result[0]&0x0f)<<8)+((result[1]&0xff));

//                        Log.e(TAG, "是否已写入数据"+result[0]+"，写入数据长度："+data_len);
//                        byte[] buf_res = new byte[data_len/2+4];
//                        if (result[0]!=0 && data_len!=0){
//                            int count = data_len/2/64;
//                            int i = 0;
//                            for (i=0; i<count; i++){
//		               	//读取数据
//                                byte[] DATA_READ = {
//                                        (byte) 0x3A,
//                                        (byte) (0x06+i*(64/4)),
//                                        (byte) (0x06+(i+1)*(64/4))
//                       //	 (byte) (5+data_len/8)
//                                };
//                                byte[] data_res = nfcA.transceive(DATA_READ);
//                                System.arraycopy(data_res, 0, buf_res, i*64, 64);
//                                Log.i(TAG, "NfcA读卡成功");
//                            }
//                            if (((data_len/2)%(64))!=0){
//                                byte[]DATA_READ = {
//                                        (byte) 0x3A,
//                                        (byte) (0x06+i*(64/4)),
//                                        (byte) (((0x06+i*(64/4))+(data_len/2/4)%(64/4))-1)
////					            (byte) (5+data_len/8)
//                                };
//                                byte[] data_res = nfcA.transceive(DATA_READ);
//                                System.arraycopy(data_res, 0, buf_res, i*64, (data_len/2)%64);
//                                Log.e(TAG, "读卡成功2");
//                            }
//                            String res = gb2312ToString(buf_res);
//                            Log.i(TAG, "NfcA--stringBytes:"+res);
////                            showNFCInfo(res);
//
//                            cardType += "NfcA数据===" +res+ "\n";
//                        }



                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {


                        try {
                            nfcA.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }





                } else if (TextUtils.equals(aTechList, "android.nfc.tech.NdeB")) {

                    cardType += "NfcB" + "\n";
                    cardType += "----------------------------------------" + "\n";

                } else if (TextUtils.equals(aTechList, "android.nfc.tech.NdeF")) {

                    cardType += "NfcF" + "\n";
                    cardType += "----------------------------------------" + "\n";

                    String ndefread = Ndefread(tag,cardType);

                    Log.e(TAG, "NdeFread" + ndefread);


                } else if (TextUtils.equals(aTechList, "android.nfc.tech.NdeV")) {
                    cardType += "NdeV" + "\n";
                    cardType += "----------------------------------------" + "\n";

                } else if (TextUtils.equals(aTechList, "android.nfc.tech.Ndef")) {
                    cardType += "Ndef" + "\n";
                    cardType += "----------------------------------------" + "\n";

                    String ndefread = Ndefread(tag,cardType);

                    Log.e(TAG, "Ndef" + ndefread);


                } else if (TextUtils.equals(aTechList, "android.nfc.tech.NdefFormatable")) {
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
//                    cardType += "最大数据尺寸:" + ndef.toString() + "字节";
//                    cardType += "最大数据尺寸:" + ndef.getMaxSize() + "字节";

                }


            }

            info += cardType;

            promt.setText("NFC信息如下：\n" + info);


        }
    }


    /**
     *
     * @param tag
     * @param cardType
     * @return  MifareClassice  读取方法
     */
    private String MifareClassiceRead(Tag tag, String cardType) {
        MifareClassic mfc = MifareClassic.get(tag);

        try {
            mfc.connect();


            int type = mfc.getType();//获取TAG的类型
            int sectorCount = mfc.getSectorCount();//获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }
            String metaInfo="";
            for (int j = 0; j < sectorCount; j++) {
                //Authenticate a sector with key A.
                boolean auth = mfc.authenticateSectorWithKeyA(j, MifareClassic.KEY_DEFAULT);
                int bCount;
                int bIndex;
                if (auth) {
                    metaInfo += "Sector " + j + ":验证成功\n";
                    // 读取扇区中的块
                    bCount = mfc.getBlockCountInSector(j);
                    bIndex = mfc.sectorToBlock(j);
                    for (int i = 0; i < bCount; i++) {
                        byte[] data = mfc.readBlock(bIndex);
                        metaInfo += "Block " + bIndex + " : "
                                + bytesToHexString(data) + "\n"+
                                 (new String(data,"utf-8")).trim() + "\n";
                        bIndex++;
                    }
                }

            }
            cardType += "MifareClassic--卡片类型="+typeS+"\n"+metaInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {


            try {
                mfc.close();
            } catch (Exception e) {
            }

        }
        return cardType;
    }

    /**
     * 数组转换成十六进制字符串
     *
     * @param bArray
     * @return
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }



    //将数据转换为GB2312
    private String gb2312ToString(byte[] data) {
        String str = null;
        try {
            str = new String(data, "gb2312");//"utf-8"
        } catch (UnsupportedEncodingException e) {
        }
        return str;
    }


    /**
     *
     * @param tag
     * @return
     * @throws IOException
     * @throws FormatException
     */
    private String Ndefread(Tag tag,String cardType)   {
        if (tag != null) {
            //解析Tag获取到NDEF实例
            Ndef ndef = Ndef.get(tag);
            String str="";
            //打开连接
            try {
                ndef.connect();
                NdefMessage message = null;
                try {
                    //获取NDEF消息
                    message = ndef.getNdefMessage();
                    //将消息转换成字节数组
                    byte[] data = message.toByteArray();
                    //将字节数组转换成字符串
                     str = new String(data, Charset.forName("UTF-8"));
                } catch (FormatException e) {
                    e.printStackTrace();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }finally {

                //关闭连接
                try {
                    ndef.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


            return cardType+"-"+str+"\n";
        } else {
            Toast.makeText(NfcScanActivity.this, "设备与nfc卡连接断开，请重新连接...",
                    Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    //推送给其他设备的

    @Override
    public NdefMessage createNdefMessage(NfcEvent event) {
        String text = ("Beam me up, Android!\n\n" +
                "Beam Time: " + System.currentTimeMillis());
        NdefMessage msg = new NdefMessage(
                new NdefRecord[] { createMime(
                        "application/vnd.com.example.android.beam", text.getBytes())
                        /**
                         * The Android Application Record (AAR) is commented out. When a device
                         * receives a push with an AAR in it, the application specified in the AAR
                         * is guaranteed to run. The AAR overrides the tag dispatch system.
                         * You can add it back in to guarantee that this
                         * activity starts when receiving a beamed message. For now, this code
                         * uses the tag dispatch system.
                        */
                        //,NdefRecord.createApplicationRecord("com.example.android.beam")
                });
        return msg;
    }
}
