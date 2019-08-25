package com.example.organizze.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.organizze.R;
import com.example.organizze.adapter.MovementsAdapter;
import com.example.organizze.config.FirebaseConfiguration;
import com.example.organizze.helper.Base64Custom;
import com.example.organizze.model.Movimentation;
import com.example.organizze.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private MaterialCalendarView calendarView;

    private TextView textUserName, textBalance;
    private Double totalExpense = 0.0;
    private Double totalIncome = 0.0;
    private Double userSummary = 0.0;
    private RecyclerView recyclerViewMovements;
    private MovementsAdapter adapter;


    private DatabaseReference reference = FirebaseConfiguration.getFirebaseDatabase();
    private FirebaseAuth auth = FirebaseConfiguration.getFirebaseAuthentication();
    private DatabaseReference userRef;
    private ValueEventListener userValueEventListener;
    private ValueEventListener movimentationsValueEventListener;
    private DatabaseReference movRef;
    private String monthYearSelected;


    private List<Movimentation> movimentationList = new ArrayList<>();
    private Movimentation movimentation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Organnize");


        textUserName = findViewById(R.id.textUserName);
        textBalance = findViewById(R.id.textBalance);


        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);

        calendarView = findViewById(R.id.calendarView);


    }

    public void getMovimentations() {
        String userEmail = auth.getCurrentUser().getEmail();
        String userId = Base64Custom.encodeBase64(userEmail);
        movRef = reference
                .child("movimentacao")
                .child(userId)
                .child(monthYearSelected);

        movimentationsValueEventListener = movRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                movimentationList.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    Movimentation movimentation = data.getValue(Movimentation.class);
                    movimentation.setKey(data.getKey());
                    movimentationList.add(movimentation);


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeMovimentation(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewMovements);

    }

    public void removeMovimentation(final RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Excluir movimentação da conta");
        alert.setMessage("Você tem certeza que deseja excluir essa movimentação da sua conta? ");
        alert.setCancelable(false);


        alert.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = viewHolder.getAdapterPosition();
                movimentation = movimentationList.get(position);

                String userEmail = auth.getCurrentUser().getEmail();
                String userId = Base64Custom.encodeBase64(userEmail);
                movRef = reference
                        .child("movimentacao")
                        .child(userId)
                        .child(monthYearSelected);

                movRef.child(movimentation.getKey()).removeValue();
                adapter.notifyItemRemoved(position);
                updateBalance();


            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();

    }

    public void configCalendarView() {
        CharSequence[] months = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"};
        calendarView.setTitleMonths(months);

        CalendarDay currentDate = calendarView.getCurrentDate();


        String selectedMonth = String.format("%02d", currentDate.getMonth() + 1);
        monthYearSelected = (selectedMonth + "" + currentDate.getYear());

        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                String selectedMonth = String.format("%02d", date.getMonth() + 1);
                monthYearSelected = (selectedMonth + "" + date.getYear());
                movRef.removeEventListener(movimentationsValueEventListener);
                getMovimentations();

            }
        });

    }

    public void updateBalance(){
        String userEmail = auth.getCurrentUser().getEmail();
        String userId = Base64Custom.encodeBase64(userEmail);
        userRef = reference.child("usuarios").child(userId);


        if(movimentation.getType().equals("r")){
            totalIncome = totalIncome - movimentation.getValue();
            userRef.child("totalIncome").setValue(totalIncome);
        }
        if(movimentation.getType().equals("d")){
            totalExpense = totalExpense - movimentation.getValue();
            userRef.child("totalExpense").setValue(totalExpense);
        }
    }


    public void addExpense(View view) {
        startActivity(new Intent(PrincipalActivity.this, ExpenseActivity.class));
    }

    public void addIncome(View view) {
        startActivity(new Intent(PrincipalActivity.this, IncomeActivity.class));
    }

    public void getSummary() {
        String userEmail = auth.getCurrentUser().getEmail();
        String userId = Base64Custom.encodeBase64(userEmail);
        userRef = reference.child("usuarios").child(userId);

        userValueEventListener = userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);//return user object
                totalExpense = user.getTotalExpense();
                totalIncome = user.getTotalIncome();
                userSummary = totalIncome - totalExpense;

                DecimalFormat decimalFormat = new DecimalFormat("0.##");
                String formattedResult = decimalFormat.format(userSummary);

                textUserName.setText("Olá, " + user.getName());
                textBalance.setText("R$" + formattedResult);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_log_out, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sign_out:
                auth.signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;

        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        configCalendarView();

        getMovimentations();
        configRecyclerViewMovements();
        getSummary();
        swipe();

    }

    @Override
    protected void onStop() {
        super.onStop();
        userRef.removeEventListener(userValueEventListener);
        movRef.removeEventListener(movimentationsValueEventListener);
    }

    public void configRecyclerViewMovements() {
        recyclerViewMovements = findViewById(R.id.recyclerMovements);
        adapter = new MovementsAdapter(movimentationList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewMovements.setLayoutManager(layoutManager);
        recyclerViewMovements.setHasFixedSize(true);
        recyclerViewMovements.setAdapter(adapter);
    }

}
