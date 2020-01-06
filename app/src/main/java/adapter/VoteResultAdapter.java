package adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onlinevotingsystem.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import model.VoteRes;
import reusable.Reusable;

public class VoteResultAdapter extends RecyclerView.Adapter<VoteResultAdapter.VoteResultViewHolder>{

    Context mContext;
    List<VoteRes> votingCandidateList;
    String token;
    int candidate_id, pos_id, voter_id;

    public VoteResultAdapter(Context mContext, List<VoteRes> votingCandidateList) {
        this.mContext = mContext;
        this.votingCandidateList = votingCandidateList;
    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @NonNull
    @Override
    public VoteResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_vote_result, viewGroup, false);
        return new VoteResultAdapter.VoteResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoteResultViewHolder voteResultViewHolder, int i) {
        final VoteRes voteCandidate = votingCandidateList.get(i);
        voteResultViewHolder.tvName.setText(voteCandidate.getLabel());
        voteResultViewHolder.votes.setText(voteCandidate.getY()+"");

        final String imagePath = Reusable.BASE_URL+voteCandidate.getPath();
        StrictMode();
        try {
            URL url = new URL(imagePath);
            voteResultViewHolder.imgVoter.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return votingCandidateList.size();
    }


    public class VoteResultViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, votes;
        CircleImageView imgVoter;

        public VoteResultViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName= itemView.findViewById(R.id.name);
            votes= itemView.findViewById(R.id.votes);
            imgVoter= itemView.findViewById(R.id.imgVoterCandidate);
        }
    }
}
