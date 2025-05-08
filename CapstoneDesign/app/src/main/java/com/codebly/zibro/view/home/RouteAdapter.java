package com.codebly.zibro.view.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codebly.zibro.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private JSONArray routes;
    private OnRouteSelectedListener onRouteSelectedListener;



    public RouteAdapter(JSONArray routes, OnRouteSelectedListener onRouteSelectedListener) {
        this.routes = routes;
        this.onRouteSelectedListener = onRouteSelectedListener;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        try {
            JSONObject route = routes.getJSONObject(position);
            JSONObject leg = route.getJSONArray("legs").getJSONObject(0);
            JSONArray steps = leg.getJSONArray("steps");
            StringBuilder transitInfo = new StringBuilder();

            String duration = leg.getJSONObject("duration").getString("text");
            String distance = leg.getJSONObject("distance").getString("text");

            // 대중교통 정보 추출
            for (int i = 0; i < steps.length(); i++) {
                JSONObject step = steps.getJSONObject(i);
                String travelMode = step.getString("travel_mode");

                if (travelMode.equals("WALKING")) {
                    // 도보 구간 처리
                    String walkDuration = step.getJSONObject("duration").getString("text");
                    transitInfo.append("도보: ").append(walkDuration).append("\n");
                } else if (travelMode.equals("TRANSIT")) {
                    // 대중교통 구간 처리
                    JSONObject transitDetails = step.getJSONObject("transit_details");
                    String vehicleType = transitDetails.getJSONObject("line").getJSONObject("vehicle").getString("type");
                    String lineName = transitDetails.getJSONObject("line").getString("short_name");
                    String departureStop = transitDetails.getJSONObject("departure_stop").getString("name");
                    String arrivalStop = transitDetails.getJSONObject("arrival_stop").getString("name");
                    String numStops = transitDetails.getString("num_stops");

                    transitInfo.append(vehicleType).append(" (").append(lineName).append(") - ")
                            .append(departureStop).append(" -> ").append(arrivalStop)
                            .append(" (정류장: ").append(numStops).append(")\n");
                }
            }

            holder.routeSummary.setText("경로 " + (position + 1));
            holder.routeDuration.setText("예상 시간: " + duration);
            holder.routeDistance.setText("거리: " + distance);
            holder.transitInfo.setText(transitInfo.toString());

            holder.itemView.setOnClickListener(v -> onRouteSelectedListener.onRouteSelected(position, duration));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return routes.length();
    }

    public static class RouteViewHolder extends RecyclerView.ViewHolder {

        TextView routeSummary, routeDuration, routeDistance, transitInfo;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            routeSummary = itemView.findViewById(R.id.route_summary);
            routeDuration = itemView.findViewById(R.id.route_duration);
            routeDistance = itemView.findViewById(R.id.route_distance);
            transitInfo = itemView.findViewById(R.id.transit_info);  // 새로운 TextView 추가
        }
    }

    public interface OnRouteSelectedListener {
        void onRouteSelected(int position, String duration);
    }
}
