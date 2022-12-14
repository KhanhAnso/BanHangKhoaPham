package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.doantrasua.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ultil.CheckConnection;
import ultil.Server;

public class ThongTinKhachHang extends AppCompatActivity {

    EditText edttenkhachhang, edtemail, edtsdt;
    Button btnxacnhan, btntrove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_khach_hang);

        anhxa();
        btntrove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            EventButton();
        } else {
            CheckConnection.ShowToast_Short(getApplicationContext(), "Ban hay kiem tra lai ket noi");
        }
    }

    private void EventButton() {
        btnxacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ten = edttenkhachhang.getText().toString().trim();
                final String sdt = edtsdt.getText().toString().trim();
                final String email = edtemail.getText().toString().trim();
                if (ten.length() > 0 && sdt.length() > 0 && email.length() > 0) {
                    final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.Duongdangetthongtinkhachhang
                            , new Response.Listener<String>() {
                        @Override
                        public void onResponse(final String madonhang) {
//                        Log.d("Madonhang",response);
                            if (Integer.parseInt(madonhang) > 0) {
                                RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
                                StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Server.DuongdanDathang,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.equals("1")) {
                                                    MainActivity.manggiohang.clear();
                                                    CheckConnection.ShowToast_Short(getApplicationContext(), "B???n ???? th??m d??? li???u gi??? h??ng th??nh c??ng");
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                    CheckConnection.ShowToast_Short(getApplicationContext(), "M???i b???n ti???p t???c mua h??ng");
                                                } else {
                                                    CheckConnection.ShowToast_Short(getApplicationContext(), "D??? li???u gi??? h??ng b??? l???i");
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {

                                            }
                                        }
                                ) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        JSONArray jsonArray = new JSONArray();
                                        for (int i = 0; i < MainActivity.manggiohang.size(); i++) {
                                            JSONObject jsonObject = new JSONObject();
                                            try {
                                                jsonObject.put("madonhang", madonhang);
                                                jsonObject.put("masanpham", MainActivity.manggiohang.get(i).getIdsp());
                                                jsonObject.put("tensanpham", MainActivity.manggiohang.get(i).getTensp());
                                                jsonObject.put("giasanpham", MainActivity.manggiohang.get(i).getGiasp());
                                                jsonObject.put("soluongsanpham", MainActivity.manggiohang.get(i).getSoluongsp());
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            jsonArray.put(jsonObject);
                                        }
                                        HashMap<String, String> hashMap = new HashMap<String, String>();
                                        hashMap.put("json", jsonArray.toString());
                                        return hashMap;
                                    }
                                };
                                requestQueue1.add(stringRequest1);
                            }
                        }
                    },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> hashMap = new HashMap<String, String>();
                            hashMap.put("tenkhachhang", ten);
                            hashMap.put("sodienthoai", sdt);
                            hashMap.put("email", email);
                            return hashMap;
                        }
                    };
                    requestQueue.add(stringRequest);
                } else {
                    CheckConnection.ShowToast_Short(getApplicationContext(), "B???n h??y nh???p ????? th??ng tin");
                }
            }
        });
    }

    private void anhxa() {
        edttenkhachhang = findViewById(R.id.edittexttenkhachhang);
        edtemail = findViewById(R.id.edittextemailkhachhang);
        edtsdt = findViewById(R.id.edittextsdtkhachhang);
        btnxacnhan = findViewById(R.id.buttonxacnhanmua);
        btntrove = findViewById(R.id.buttontrove);
    }
}
