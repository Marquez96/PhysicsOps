package marquez.physicsops.Activities;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.app.Activity;


import marquez.physicsops.Classes.EventCalendar;
import marquez.physicsops.Classes.EventCalendarCards;
import marquez.physicsops.Classes.Friend;
import marquez.physicsops.Classes.User;
import marquez.physicsops.R;

/**
 * Created by Carlos-Torre on 07/05/2018.
 */

public class RVAdapterEvents extends RecyclerView.Adapter<RVAdapterEvents.EventViewHolder> {

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView eventTitle;
        TextView textVierImportance;
        TextView textViewDuration;
        RelativeLayout layout;
        EventCalendar event;
        LinearLayout colorLayout;

        EventViewHolder(View itemView, final EventCalendar event) {
            super(itemView);
            layout = (RelativeLayout) itemView.findViewById(R.id.layoutRvEvent);
            eventTitle = (TextView) itemView.findViewById(R.id.title_event_card);
            textVierImportance = (TextView) itemView.findViewById(R.id.event_importance_card);
            textViewDuration = (TextView) itemView.findViewById(R.id.event_duration_card);
            colorLayout = (LinearLayout) itemView.findViewById(R.id.linearColorEvent);
            this.event = event;
        }
    }

    User user;
    List<EventCalendar> events;
    EventCalendarCards cards;

    RVAdapterEvents(User user, List<EventCalendar> events, EventCalendarCards cards) {
        this.user = user;
        this.events = events;
        this.cards = cards;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RVAdapterEvents.EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_event, viewGroup, false);
        RVAdapterEvents.EventViewHolder pvh = new RVAdapterEvents.EventViewHolder(v, events.get(i));
        return pvh;
    }

    @Override
    public void onBindViewHolder(final RVAdapterEvents.EventViewHolder userViewHolder, final int i) {
        userViewHolder.eventTitle.setText("Titulo: " + events.get(i).getTitle());
        userViewHolder.textViewDuration.setText("Duracion: " + events.get(i).getDuration() + "h");
        userViewHolder.textVierImportance.setText("Importancia: " + events.get(i).getImportance());
        userViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CardLog", events.get(i).toString());
            }
        });
        userViewHolder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                cards.selectLongEvent(v,i,userViewHolder.colorLayout);
                return false;
            }
        });
        userViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cards.selectShortEvent(v,i,userViewHolder.layout).equals("doubleClick")){
                    Intent addEventIntent = new Intent(v.getContext(), AddEventActivity.class);
                    addEventIntent.putExtra("user", user);
                    addEventIntent.putExtra("dayEvent", events.get(i).getCalendar());
                    addEventIntent.putExtra("event", events.get(i));
                    ((Activity) v.getContext()).startActivityForResult(addEventIntent, 1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}