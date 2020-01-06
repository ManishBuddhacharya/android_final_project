package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.onlinevotingsystem.R;

import java.util.List;

import model.Position;

public class PositionAdapterForMember extends RecyclerView.Adapter<PositionAdapterForMember.PositionViewHolderForMember>{
    Context mContext;
    List<Position> positionList;

    public PositionAdapterForMember(Context mContext, List<Position> positionList) {
        this.mContext = mContext;
        this.positionList = positionList;
    }

    @NonNull
    @Override
    public PositionViewHolderForMember onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_position_for_member, viewGroup, false);
        return new PositionAdapterForMember.PositionViewHolderForMember(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PositionViewHolderForMember positionViewHolderForMember, int i) {
        final Position position = positionList.get(i);

        positionViewHolderForMember.tvPositionName.setText(position.getName());
    }

    @Override
    public int getItemCount() {
        return positionList.size();
    }

    public class PositionViewHolderForMember extends RecyclerView.ViewHolder{
        TextView tvPositionName;

        public PositionViewHolderForMember(@NonNull View positionView) {
            super(positionView);
            tvPositionName= positionView.findViewById(R.id.tvPositionName);
        }
    }
}
