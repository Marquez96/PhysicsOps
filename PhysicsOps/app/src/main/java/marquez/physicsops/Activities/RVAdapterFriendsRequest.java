package marquez.physicsops.Activities;

/**
 * Created by Carlos-Torre on 02/05/2018.
 */

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import marquez.physicsops.Classes.*;
import marquez.physicsops.R;

import java.util.List;
import android.util.Log;
import android.widget.Toast;

public class RVAdapterFriendsRequest extends RecyclerView.Adapter<RVAdapterFriendsRequest.FriendRequestViewHolder> {

    public static class FriendRequestViewHolder extends RecyclerView.ViewHolder {

        TextView titleFriendRequest;
        TextView accept;
        TextView reject;
        Friend user;

        FriendRequestViewHolder(View itemView, final Friend user) {
            super(itemView);
            titleFriendRequest = (TextView)itemView.findViewById(R.id.titleFriendRequest);
            accept = (TextView) itemView.findViewById(R.id.acceptFriendRequest);
            reject = (TextView) itemView.findViewById(R.id.rejectFriendRequest);
            this.user = user;
        }
    }
    User user;
    List<Friend> requestUsers;
    FriendshipActivity activity;


    RVAdapterFriendsRequest(User user, List<Friend> requestUsers, FriendshipActivity activity){
        this.user = user;
        this.requestUsers = requestUsers;
        this.activity = activity;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public FriendRequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_friend_request, viewGroup, false);
        FriendRequestViewHolder pvh = new FriendRequestViewHolder(v, requestUsers.get(i));
        return pvh;
    }

    @Override
    public void onBindViewHolder(FriendRequestViewHolder userViewHolder, final int i) {
        userViewHolder.titleFriendRequest.setText("Peticion de: " + requestUsers.get(i).getIdUserFrom());
        userViewHolder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CardLog",requestUsers.get(i).toString() +"Accept");
                requestUsers.get(i).setState("friend");
                activity.resetRecyclerView();
                Toast.makeText(v.getContext(), "Se ha aceptado la peticion de " + requestUsers.get(i).getIdUserFrom(), Toast.LENGTH_SHORT).show();
                DBSQLite db = new DBSQLite(v.getContext(), v.getResources().getString(R.string.nameDB), null, v.getResources().getInteger(R.integer.versionSQLite));
                FacadeSQLite sql = new FacadeSQLite(db);
                sql.updateFriend(user,requestUsers.get(i));
            }
        });
        userViewHolder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CardLog",requestUsers.get(i).toString() +"Reject");
                requestUsers.get(i).setState("deleted");
                activity.resetRecyclerView();
                Toast.makeText(v.getContext(), "Se ha rechazado la peticion de " + requestUsers.get(i).getIdUserFrom(), Toast.LENGTH_SHORT).show();
                DBSQLite db = new DBSQLite(v.getContext(), v.getResources().getString(R.string.nameDB), null, v.getResources().getInteger(R.integer.versionSQLite));
                FacadeSQLite sql = new FacadeSQLite(db);
                sql.deleteFriend(user, requestUsers.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestUsers.size();
    }
}