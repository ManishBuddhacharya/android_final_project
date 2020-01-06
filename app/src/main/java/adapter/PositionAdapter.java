package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.onlinevotingsystem.BLL.PositionBLL;
import com.onlinevotingsystem.CandidateActivity;
import com.onlinevotingsystem.EditPositionActivity;
import com.onlinevotingsystem.PositionActivity;
import com.onlinevotingsystem.R;
import java.util.List;
import API.API;
import model.Position;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class PositionAdapter extends RecyclerView.Adapter<PositionAdapter.PositionViewHolder> {
    Context mContext;
    List<Position> positionList;

    public PositionAdapter(Context mContext, List<Position> positionList) {
        this.mContext = mContext;
        this.positionList = positionList;
    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }


    @NonNull
    @Override
    public PositionViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_position, viewGroup, false);
        return new PositionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PositionViewHolder positionViewHolder, int i) {
        final Position position = positionList.get(i);

        positionViewHolder.tvPositionName.setText(position.getName());

        positionViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API Api = Reusable.getInstance().create(API.class);
                int id =position.getId();
                SharedPreferences sharedPreference = mContext.getSharedPreferences("token", mContext.MODE_PRIVATE);
                String token = sharedPreference.getString("token","");
                Call<Position> listCall = Api.searchPosition(token, id);

                listCall.enqueue(new Callback<Position>() {
                    @Override
                    public void onResponse(Call<Position> call, Response<Position> response) {
                        Toast.makeText(mContext,response.body().toString(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(mContext, EditPositionActivity.class);
                        intent.putExtra("name", position.getName());
                        intent.putExtra("id", position.getId());
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Position> call, Throwable t) {
                        Toast.makeText(mContext, "Error ",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        positionViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id =position.getId();
                deletePosition(id);
            }
        });
    }


    @Override
    public int getItemCount() {
        return positionList.size();
    }

    public class PositionViewHolder extends RecyclerView.ViewHolder{
        TextView tvPositionName;
        Button btnEdit, btnDelete;

        public PositionViewHolder(@NonNull View positionView) {
            super(positionView);
            tvPositionName= positionView.findViewById(R.id.tvPositionName);
            btnEdit= positionView.findViewById(R.id.btnEdit);
            btnDelete= positionView.findViewById(R.id.btnDelete);
        }
    }

    private void deletePosition(final int id) {
        new AlertDialog.Builder(mContext).setTitle("Delete Position!").setMessage("Do you really want Delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog,int whichButton){

                        final PositionBLL bll = new PositionBLL();
                        StrictMode();
                        SharedPreferences sharedPreference = mContext.getSharedPreferences("token", mContext.MODE_PRIVATE);
                        String token = sharedPreference.getString("token","");
                        if (bll.deletePosition(token, id)){
                            Toast.makeText(mContext, "Successful Deleted;", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(mContext, PositionActivity.class);
                            mContext.startActivity(intent);
                            ((PositionActivity)mContext).finish();
                        }
                        else {
                            Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton(android.R.string.no, null).show();

    }
}
