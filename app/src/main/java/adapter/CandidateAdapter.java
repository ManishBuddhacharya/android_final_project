package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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

import com.onlinevotingsystem.BLL.CandidateBLL;
import com.onlinevotingsystem.CandidateActivity;
import com.onlinevotingsystem.EditCandidateActivity;
import com.onlinevotingsystem.EditPositionActivity;
import com.onlinevotingsystem.PositionActivity;
import com.onlinevotingsystem.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import API.API;
import de.hdodenhof.circleimageview.CircleImageView;
import model.Candidate;
import model.Position;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder>{
    Context mContext;
    List<Candidate> candidateList;

    public CandidateAdapter(Context mContext, List<Candidate> candidateList) {
        this.mContext = mContext;
        this.candidateList = candidateList;
    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @NonNull
    @Override
    public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_candidate, viewGroup, false);
        return new CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolder candidateViewHolder, int i) {
        final Candidate candidate = candidateList.get(i);

        final String imagePath = Reusable.BASE_URL+candidate.getPath();
        StrictMode();
        try {
            URL url = new URL(imagePath);
            candidateViewHolder.imgCandidate.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        candidateViewHolder.tvCandidateName.setText("Name: "+candidate.getUser_name()+"\n Position : "+candidate.getPos_name());

        candidateViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API Api = Reusable.getInstance().create(API.class);
                int id =candidate.getId();
                SharedPreferences sharedPreference = mContext.getSharedPreferences("token", mContext.MODE_PRIVATE);
                String token = sharedPreference.getString("token","");
                Call<Candidate> listCall = Api.searchCandidate(token, id);

                listCall.enqueue(new Callback<Candidate>() {
                    @Override
                    public void onResponse(Call<Candidate> call, Response<Candidate> response) {
                        Toast.makeText(mContext,response.body().toString(), Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(mContext, EditCandidateActivity.class);
                        intent.putExtra("pos_id", candidate.getPosition_id());
                        intent.putExtra("pos_name", candidate.getPos_name());
                        intent.putExtra("user_id", candidate.getUser_id());
                        intent.putExtra("mem_name", candidate.getUser_name());
                        intent.putExtra("id", candidate.getId());
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<Candidate> call, Throwable t) {
                        Toast.makeText(mContext, "Error ",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        candidateViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id =candidate.getId();
                deleteCandidate(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public class CandidateViewHolder extends RecyclerView.ViewHolder{
        TextView tvCandidateName;
        Button btnEdit, btnDelete;
        CircleImageView imgCandidate;

        public CandidateViewHolder(@NonNull View candidateView) {
            super(candidateView);
            tvCandidateName= candidateView.findViewById(R.id.tvCandidateName);
            imgCandidate= candidateView.findViewById(R.id.imgCandidatePic);
            btnEdit= candidateView.findViewById(R.id.btnEdit);
            btnDelete= candidateView.findViewById(R.id.btnDelete);
        }
    }

    private void deleteCandidate(final int id) {
        new AlertDialog.Builder(mContext).setTitle("Delete Position!").setMessage("Do you really want Delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick (DialogInterface dialog,int whichButton){
                        final CandidateBLL bll = new CandidateBLL();
                        StrictMode();
                        SharedPreferences sharedPreference = mContext.getSharedPreferences("token", mContext.MODE_PRIVATE);
                        String token = sharedPreference.getString("token","");
                        if (bll.deleteCandidate(token, id)){
                            Toast.makeText(mContext, "Successful Deleted;", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(mContext, CandidateActivity.class);
                            mContext.startActivity(intent);
                            ((CandidateActivity)mContext).finish();
                        }
                        else {
                            Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }).setNegativeButton(android.R.string.no, null).show();

    }
}
