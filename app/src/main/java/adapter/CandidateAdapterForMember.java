package adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.onlinevotingsystem.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Candidate;
import reusable.Reusable;

public class CandidateAdapterForMember extends RecyclerView.Adapter<CandidateAdapterForMember.CandidateViewHolderForMember> {
    Context mContext;
    List<Candidate> candidateList;

    public CandidateAdapterForMember(Context mContext, List<Candidate> candidateList) {
        this.mContext = mContext;
        this.candidateList = candidateList;
    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @NonNull
    @Override
    public CandidateViewHolderForMember onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_candidate_for_member, viewGroup, false);
        return new CandidateAdapterForMember.CandidateViewHolderForMember(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CandidateViewHolderForMember candidateViewHolderForMember, int i) {
        final Candidate candidate = candidateList.get(i);

        final String imagePath = Reusable.BASE_URL+candidate.getPath();
        StrictMode();
        try {
            URL url = new URL(imagePath);
            candidateViewHolderForMember.imgCandidate.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        candidateViewHolderForMember.tvCandidateName.setText("Name: "+candidate.getUser_name()+"\n Position : "+candidate.getPos_name());
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public class CandidateViewHolderForMember extends RecyclerView.ViewHolder{
        TextView tvCandidateName;
        CircleImageView imgCandidate;

        public CandidateViewHolderForMember(@NonNull View candidateView) {
            super(candidateView);
            tvCandidateName= candidateView.findViewById(R.id.tvCandidateName);
            imgCandidate= candidateView.findViewById(R.id.imgCandidatePic);
        }
    }
}
