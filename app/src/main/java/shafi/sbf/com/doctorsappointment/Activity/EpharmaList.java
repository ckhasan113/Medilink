package shafi.sbf.com.doctorsappointment.Activity;

import android.app.Dialog;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shafi.sbf.com.doctorsappointment.EPharma.EpharmaOrderList;
import shafi.sbf.com.doctorsappointment.R;
import shafi.sbf.com.doctorsappointment.adapter.ProductListAdapter;
import shafi.sbf.com.doctorsappointment.dialog.LoadingDialog;
import shafi.sbf.com.doctorsappointment.pojo.ConfirmOrder;
import shafi.sbf.com.doctorsappointment.pojo.EPharmaDetails;
import shafi.sbf.com.doctorsappointment.pojo.OrderChart;
import shafi.sbf.com.doctorsappointment.pojo.PatientDetails;
import shafi.sbf.com.doctorsappointment.pojo.PharmacyProductDetails;

public class EpharmaList extends AppCompatActivity implements ProductListAdapter.ProductListAdapterListener {

    CardView Medicin1;

    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference productRef;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;


    private DatabaseReference patiRef;


    private double totalPrice;

    int it = 0;

    private ConfirmOrder confirmOrder;

    private LoadingDialog dialog;

    private ImageView vendorImageIV;

    private TextView vendorNmeTV, vendorAddressTV, vendorEstablishTV, uploadPhotoTV, savePhotoTV;

    private RadioButton productRB, requestRB;

    private Button addNewProductBtn;

    private RecyclerView baseRecycler;

    private StorageReference storageReference;

    private String userID, photoLink, name, registrationNumber, owner, area , city, establishYear, phone, email, password;

    private String Type = "Full Box";

    private String PriceByQuen,quentity;

    private ProductListAdapter productListAdapter;

    private List<OrderChart> orderCharts = new ArrayList<OrderChart>();

    Dialog mydialog;

    FrameLayout frameLayout;

    private int i ;

    PatientDetails patientDetails;

    private List<PharmacyProductDetails> productList = new ArrayList<PharmacyProductDetails>();

    TextView Item;
    private String patient_id;

    private boolean checkNothing = false;
    private EPharmaDetails vendorList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epharma_list);

        dialog = new LoadingDialog(EpharmaList.this,"Loading...");
        dialog.show();

        vendorList = (EPharmaDetails) getIntent().getSerializableExtra("VendorDetails");

        String checkamade = getIntent().getStringExtra("chec");

        if (checkamade.equals("11")){
            confirmOrder =(ConfirmOrder) getIntent().getSerializableExtra("curt");

            orderCharts = confirmOrder.getChartList();
            frameLayout.setVisibility(View.VISIBLE);
        }

        userID = vendorList.getId();

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child("Pharmacy").child(userID);
        productRef = userRef.child("Product");

        patient_id = user.getUid();
        patiRef = rootRef.child("Patient").child(patient_id);

        getPatientDetails();

        mydialog = new Dialog(this);

        frameLayout = findViewById(R.id.fram);
        Item = findViewById(R.id.item);

        vendorImageIV = findViewById(R.id.vendor_image);
        vendorNmeTV = findViewById(R.id.vendor_name);
        vendorAddressTV = findViewById(R.id.vendor_address);
        vendorEstablishTV = findViewById(R.id.vendor_establish);

        photoLink = vendorList.getImage();
        name = vendorList.getFarmName();
        registrationNumber = vendorList.getTinNumber();
        area = vendorList.getArea();
        city = vendorList.getCity();
        establishYear = vendorList.getEstablishYear();
        owner = vendorList.getOwner();
        phone = vendorList.getPhone();
        email = vendorList.getEmail();
        password = vendorList.getPassword();

        Picasso.get().load(Uri.parse(photoLink)).into(vendorImageIV);

        //vendorImageIV.setImageURI(Uri.parse(photoLink));
        vendorNmeTV.setText(name);
        vendorAddressTV.setText(area+", "+city);
        vendorEstablishTV.setText("Establish year: "+establishYear);


        baseRecycler = findViewById(R.id.product_and_request_base_recycler);


        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    PharmacyProductDetails pd = d.child("Details").getValue(PharmacyProductDetails.class);
                    productList.add(pd);
                }
                Collections.reverse(productList);
                productListAdapter = new ProductListAdapter(EpharmaList.this, productList);
                LinearLayoutManager llm = new LinearLayoutManager(EpharmaList.this);
                llm.setOrientation(RecyclerView.VERTICAL);
                baseRecycler.setLayoutManager(llm);
                baseRecycler.setAdapter(productListAdapter);
                dialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onDatabase();
            }
        });


    }

    private void getPatientDetails() {
        patiRef.child("Details").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                patientDetails = dataSnapshot.getValue(PatientDetails.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onDatabase() {

        String id = "PUSH ID";
        totalPrice = 0;
        for (OrderChart c: orderCharts){
            double price = Double.parseDouble(c.getPrice());
            totalPrice += price;
        }
        String includeVatPrice = String.valueOf((totalPrice+(totalPrice*15.0)/100.0));

        ConfirmOrder confi = new ConfirmOrder(id,orderCharts,patientDetails,vendorList,String.valueOf(totalPrice),includeVatPrice,"nai","Req");

        Intent intent = new Intent(new Intent(EpharmaList.this, EpharmaOrderList.class));
        intent.putExtra("curt",confi);
        startActivity(intent);
        finish();

    }


    @Override
    public void getProductDetails(PharmacyProductDetails product) {
        addToChart(product);
    }

    private void addToChart(final PharmacyProductDetails product) {


        i=1;
        //Dialog open
        mydialog.setContentView(R.layout.custom_addtocurt_medicine);
        Button txtclose =(Button) mydialog.findViewById(R.id.txtclose2);
        TextView txtname =(TextView) mydialog.findViewById(R.id.medi_name);
        final TextView txtprice =(TextView) mydialog.findViewById(R.id.medi_pri);
        TextView txtcompany =(TextView) mydialog.findViewById(R.id.medi_com);
        final TextView txtquan =(TextView) mydialog.findViewById(R.id.medi_quan);
        ImageView imageView =(ImageView) mydialog.findViewById(R.id.medi_image);
        ImageView minus =(ImageView) mydialog.findViewById(R.id.medi_minus);
        ImageView pluse =(ImageView) mydialog.findViewById(R.id.medi_plus);

        RadioButton full =(RadioButton) mydialog.findViewById(R.id.radio0);
        RadioButton single =(RadioButton) mydialog.findViewById(R.id.radio1);

        full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type = "Full Box";
            }
        });

        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Type = "Custom";
            }
        });

        pluse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++i;
                Double pric  =Double.parseDouble(product.getPrice()) *i;

                PriceByQuen = String.valueOf(pric);

                txtquan.setText(String.valueOf(i));
                txtprice.setText("BDT "+PriceByQuen+" TK");
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (i>1) {
                    --i;

                    Double pric = Double.parseDouble(product.getPrice()) * i;

                    PriceByQuen = String.valueOf(pric);

                    txtquan.setText(String.valueOf(i));
                    txtprice.setText("BDT "+PriceByQuen+" TK");
                }

            }
        });

        txtprice.setText(PriceByQuen);

        Double pric  =Double.parseDouble(product.getPrice()) *i;

        PriceByQuen = String.valueOf(pric);

        for (OrderChart c: orderCharts){
            if (c.getProductDetails().getName().equals(product.getName()) && c.getProductDetails().getCompany().equals(product.getCompany())){

                txtquan.setText(c.getQuentity());
                txtprice.setText("BDT "+c.getPrice()+" TK");
                i=Integer.parseInt(c.getQuentity());
                orderCharts.remove(c);

                break;
            }else {

                txtquan.setText(String.valueOf(i));
                txtprice.setText("BDT "+PriceByQuen+" TK");
            }
        }

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.dismiss();
            }
        });

        txtname.setText(product.getName());
        txtprice.setText("BDT "+PriceByQuen+" TK");
        txtcompany.setText(product.getCompany());

        Picasso.get().load(Uri.parse(product.getImage())).into(imageView);

        Button confirm = mydialog.findViewById(R.id.cart);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialog(product);
                frameLayout.setVisibility(View.VISIBLE);
                mydialog.dismiss();
            }
        });

        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mydialog.show();

    }

    private void openDialog(PharmacyProductDetails product) {

        OrderChart chart = new OrderChart(String.valueOf(i),product, PriceByQuen,Type);
        orderCharts.add(chart);
        Item.setText(String.valueOf(orderCharts.size()));
    }


}
