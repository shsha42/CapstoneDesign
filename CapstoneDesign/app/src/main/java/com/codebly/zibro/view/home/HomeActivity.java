package com.codebly.zibro.view.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.codebly.zibro.BuildConfig;
import com.codebly.zibro.view.home.menu.alarm.NotiActivity;
import com.codebly.zibro.view.home.menu.alarm.NotificationAdapter;
import com.codebly.zibro.view.home.menu.alarm.NotificationItem;
import com.codebly.zibro.view.home.menu.customerservice.CustomerServiceActivity;
import com.codebly.zibro.view.home.menu.friends.FriendPageActivity;
import com.codebly.zibro.view.home.menu.alarm.AlarmActivity;
import com.codebly.zibro.view.home.menu.mypage.MyPageActivity;
import com.codebly.zibro.view.home.sos.sosbuttton;
import com.codebly.zibro.view.login.LoginActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.codebly.zibro.R;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.Marker;
import android.os.Handler;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.google.gson.reflect.TypeToken;


//세빈 import
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.preference.PreferenceManager;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class HomeActivity extends AppCompatActivity implements  OnMapReadyCallback {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MapView mapView;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final int LOCATION_REQUEST_CODE = 10001;

    private boolean isCameraMoved = false;
    private Marker currentMarker;
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private ImageView searchImageView;
    private ImageView search_button;
    private LatLng selectedLocation;
    private TextView estimatedTimeTextView;
    private ImageView startButtonImageView;
    private long lastUpdateTime = 0;
    private int remainingMinutes = 0;
    private Handler handler = new Handler();
    private Runnable updateTimeRunnable;
    private Polyline currentPolyline;
    private Marker destinationMarker;
    private boolean isRouteSelected = false;
    private Circle proximityCircle;
    private ImageView stopButtonImageView;
    private ImageView pauseButtonImageView;
    private LinearLayout buttonContainer;
    private TextView getEstimatedTimeTextView;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationItem> notificationList;

    private static final String DIRECTIONS_API_KEY = BuildConfig.API_KEY;

    private String userName = "사용자";
    private static final int LOCATION_REQUEST_REQUEST_CODE = 1000; // 적절한 요청 코드 설정
    private NotificationAdapter adapter;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LatLng currentLocation; // 현재 위치를 저장할 변수

    private FusedLocationProviderClient fusedLocationClient;

    private boolean isDialogShowing = false; // 다이얼로그 상태를 나타내는 변수

    private FirebaseFirestore db;
    private Marker friendMarker;

    private float currentZoomLevel = 15.0f;
    private String friendUserId = "xrS5akNYLLOWANp4okJVHZqH6k22";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        ImageView Profile_image = headerView.findViewById(R.id.Profile_image);
        ImageView my_information_setting = headerView.findViewById(R.id.my_information_setting);
        ImageView help_button = headerView.findViewById(R.id.help_button);
        ImageView customer_service_center = headerView.findViewById(R.id.customer_service_center);
        ImageView Check_alarm_button = headerView.findViewById(R.id.Check_alarm_button);
        ImageView alarm_setting = headerView.findViewById(R.id.alarm_setting);
        ImageView friend_setting = headerView.findViewById(R.id.friend_setting);
        ImageView my_location_button = headerView.findViewById(R.id.my_location_button);
        ImageView Nickname = headerView.findViewById(R.id.Nickname);
        ImageView menu_button = findViewById(R.id.menu_button);



        // 위치 권한 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        // notificationList 초기화
        notificationList = new ArrayList<>();

        // 어댑터 초기화
        adapter = new NotificationAdapter(notificationList);
        estimatedTimeTextView = findViewById(R.id.estimated_time_text_view);

        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notificationList);

        // Initialize FusedLocationProviderClient for getting current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        // FusedLocationProviderClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // 현재 위치를 가져오는 코드 추가
        getLastKnownLocation();

        // Firestore 인스턴스 초기화
        db = FirebaseFirestore.getInstance();



        // 초기 위치 가져오기
        getCurrentLocation();

        // stop 버튼과 버튼 컨테이너 초기화
        ImageView stopButtonImageView = findViewById(R.id.stop_button_imageView);
        ImageView pauseButtonImageView = findViewById(R.id.pause_button_imageView);
        LinearLayout buttonContainer = findViewById(R.id.button_container);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button arriveButton = findViewById(R.id.arrive_button);

        stopButtonImageView.setVisibility(View.GONE);
        pauseButtonImageView.setVisibility(View.GONE); // 처음에 pause 버튼도 숨기기
        buttonContainer.setVisibility(View.GONE);


        // stop 버튼을 클릭했을 때
        stopButtonImageView.setOnClickListener(v -> {
            stopButtonImageView.setVisibility(View.GONE); // stop 버튼 숨기기
            pauseButtonImageView.setVisibility(View.VISIBLE); // pause 버튼 보이기
            buttonContainer.setVisibility(View.GONE); // 취소 및 도착 버튼 숨기기
        });

        pauseButtonImageView.setOnClickListener(v -> {
            pauseButtonImageView.setVisibility(View.GONE); // pause 버튼 숨기기
            stopButtonImageView.setVisibility(View.VISIBLE); // stop 버튼 보이기
            buttonContainer.setVisibility(View.VISIBLE); // 취소 및 도착 버튼 보이기
        });

        // 도착 버튼 클릭 리스너 설정
        arriveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 토스트 메시지 표시
                Toast.makeText(HomeActivity.this, "도착했습니다!", Toast.LENGTH_SHORT).show();

                /// 알림 상단의 날짜와 시간
                String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()); // 초까지 포함된 포맷

                // 알림 메시지 생성
                String locationAddress = selectedLocation != null ? getAddressFromLatLng(selectedLocation) : "목적지 정보 없음";
                String message = userName + " 님이 " + locationAddress + "에 도착하였습니다." + "\n";
                uploadNotification(currentDateTime, message);

                // 타이머 중지
                stopCountdownTimer();

                // 경로 지우기
                clearPolyline();

                // 목적지 핑 지우기
                clearDestinationMarker();

                // 목적지 좌표 초기화
                selectedLocation = null;

                // 목적지 원 지우기
                clearDestinationProximityCircle();


                // 남은 시간 텍스트 숨기기
                estimatedTimeTextView.setVisibility(View.GONE);

                stopButtonImageView.setVisibility(View.GONE);
                pauseButtonImageView.setVisibility(View.GONE); // 처음에 pause 버튼도 숨기기
                buttonContainer.setVisibility(View.GONE);
                startButtonImageView.setVisibility(View.VISIBLE);

            }
        });


        // 취소 버튼 기능
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 토스트 메시지 표시
                Toast.makeText(HomeActivity.this, "귀가를 취소했습니다.", Toast.LENGTH_SHORT).show();

                /// 알림 상단의 날짜와 시간
                String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()); // 초까지 포함된 포맷

                // 기존 메시지 생성 로직 (위치 포함)
                String locationAddress = selectedLocation != null ? getAddressFromLatLng(selectedLocation) : "목적지 정보 없음";
                String message = userName + " " + "님이 귀가를 취소하였습니다." + "\n";
                uploadNotification(currentDateTime, message);


                // 타이머 중지
                stopCountdownTimer();

                // 경로 지우기
                clearPolyline();

                // 목적지 핑 지우기
                clearDestinationMarker();

                // 목적지 좌표 초기화
                selectedLocation = null;

                // 목적지 원 지우기
                clearDestinationProximityCircle();

                // 남은 시간 텍스트 숨기기
                estimatedTimeTextView.setVisibility(View.GONE);

                stopButtonImageView.setVisibility(View.GONE);
                pauseButtonImageView.setVisibility(View.GONE); // 처음에 pause 버튼도 숨기기
                buttonContainer.setVisibility(View.GONE);
                startButtonImageView.setVisibility(View.VISIBLE);

            }
        });

        // 기본적으로 stop_button_imageView는 보이지 않도록 설정
        stopButtonImageView.setVisibility(View.GONE);
        pauseButtonImageView.setVisibility(View.GONE); // 처음에 pause 버튼도 숨기기
        buttonContainer.setVisibility(View.GONE);

        startButtonImageView = findViewById(R.id.start_button_imageView);
        ImageView pause_button = findViewById(R.id.pause_button_imageView); // pause_button 초기화


// api key 불러오기
        String apiKey = BuildConfig.API_KEY;

        // Initialize Google Places API
        Places.initialize(getApplicationContext(), apiKey);

        // Initialize MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);

        // Initialize FusedLocationProviderClient for getting current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        startButtonImageView = findViewById(R.id.start_button_imageView);
        // Set up ImageView for launching the search (Places Autocomplete)
        searchImageView = findViewById(R.id.content_imageView);
        search_button = findViewById(R.id.search_button);

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Places Autocompletev
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,
                        Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
                        .build(HomeActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        searchImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch Places Autocomplete
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN,
                        Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))
                        .build(HomeActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });


        // 홈버튼 클릭 시 친구 선택창 나오는 코드
        findViewById(R.id.start_button_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // BottomSheetDialog를 띄움
                showBottomSheetDialog();
            }
        });


        Check_alarm_button.setOnClickListener(v -> {
            // 클릭 시 새로운 Activity로 화면 전환
            Intent intent = new Intent(HomeActivity.this, NotiActivity.class);
            startActivity(intent);
        });

        // my_waypoint_button 클릭 리스터 설정
        ImageView waypointButton = findViewById(R.id.my_waypoint_button);
        waypointButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveCameraToCurrentLocation();
            }
        });

        // sos385 이미지뷰를 찾고 클릭 리스너 설정
        ImageView sosButton = findViewById(R.id.sos385);
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sos_buttton 액티비티로 이동하는 인텐트 생성
                Intent intent = new Intent(HomeActivity.this, sosbuttton.class);
                startActivity(intent);
            }
        });

        // 실시간 위치 요청 설정
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000);  // 0.1초마다 업데이트
        locationRequest.setFastestInterval(500);  // 최소 0.1초 간격
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);  // 높은 정확도

        // 위치 업데이트 콜백 설정
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }

                long currentTime = System.currentTimeMillis();

                // 마지막 업데이트 이후 5초가 지나지 않았으면 무시
                if (currentTime - lastUpdateTime < 5000) {
                    return;
                }

                // 5초가 지나면 위치 업데이트 처리
                lastUpdateTime = currentTime;

                for (Location location : locationResult.getLocations()) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();     // 내 uid를 이용해 firestore에 저장

                    // 내 uid를 가져왔다면
                    if (currentUser != null) {
                        saveLocationDataToFirestore(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                                currentLocation.latitude, currentLocation.longitude);
                        LatLng newLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    }
                    updateCurrentLocationMarker(currentLocation);

                    // 이전 마커 제거
                    if (currentMarker != null){
                        currentMarker.remove();
                    }

                    // 새로운 마커 추가
                    currentMarker = googleMap.addMarker(new MarkerOptions()
                            .position(currentLocation)
                            .title("현재 위치")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    // 앱 실행 후 최초 카메라 이동
                    if (!isCameraMoved){
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                        isCameraMoved = true;
                    }

                    // 경로가 선택된 후에만 목적지와의 거리 비교
                    if (isRouteSelected && selectedLocation != null) {
                        checkProximityToDestination(currentLocation, selectedLocation);
                        isRouteSelected = false;
                    }
                    // 위치 업데이트 요청
                    if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                    } else {
                        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                    }

                    Toast.makeText(HomeActivity.this, "새로운 위치: " + location.getLatitude() + ", " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                }
            }
        };
        //drawerlayout 시작
        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 드로어 열기
                drawerLayout.openDrawer(findViewById(R.id.nav_view));
            }
        });

        //drawerlayout 끝





        // drawerlayout에서 버튼 클릭시 화면전환 코드 시작
        alarm_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 시 새로운 Activity로 화면 전환
                Intent intent = new Intent(HomeActivity.this, AlarmActivity.class);
                startActivity(intent);

            }
        });

        friend_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 시 새로운 Activity로 화면 전환
                Intent intent = new Intent(HomeActivity.this, FriendPageActivity.class);
                startActivity(intent);

            }
        });

        my_information_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 시 새로운 Activity로 화면 전환
                Intent intent = new Intent(HomeActivity.this, MyPageActivity.class);
                startActivity(intent);

            }
        });
        //고객센터
        customer_service_center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 클릭 시 새로운 Activity로 화면 전환
                Intent intent = new Intent(HomeActivity.this, CustomerServiceActivity.class);
                startActivity(intent);

            }
        });
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 권한이 허용되었을 때만 위치 정보를 가져옴
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                            Toast.makeText(HomeActivity.this, "현재 위치를 가져왔습니다: " + currentLocation.latitude + ", " + currentLocation.longitude, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        Toast.makeText(HomeActivity.this, "위치 데이터를 가져오는 데 실패했습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            // 권한 요청
            requestLocationPermission();
        }
    }

    // requestLocationPermission() 메서드 선언 위치
    private void requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }
    }



    @Override
    protected void onStart() {
        super.onStart();


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            // 로그인되지 않은 경우 로그인 화면으로 이동
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (!currentUser.getUid().equals(friendUserId)) {
            // 위치를 공유받는 사용자일 때 친구의 위치를 구독하고 자신의 위치는 가져오지 않음
            db.collection("locations")
                    .document(friendUserId)
                    .addSnapshotListener((snapshot, e) -> {
                        if (snapshot != null && snapshot.exists()) {
                            Double latitude = snapshot.getDouble("latitude");
                            Double longitude = snapshot.getDouble("longitude");

                            if (latitude != null && longitude != null) {
                                // 친구의 위치 데이터가 올바르게 받아졌을 때 Toast 메시지로 출력
                                //runOnUiThread(() -> Toast.makeText(HomeActivity.this,
                                //        "받아온 친구 위치 데이터: " + latitude + ", " + longitude,
                                //        Toast.LENGTH_SHORT).show());

                                // 받아온 친구 위치를 지도에 표시
                                LatLng friendLocation = new LatLng(latitude, longitude);
                                drawFriendLocationOnMap(friendLocation);
                            } else {
                                runOnUiThread(() -> Toast.makeText(HomeActivity.this,
                                        "친구 위치 데이터가 유효하지 않습니다.",
                                        Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            runOnUiThread(() -> Toast.makeText(HomeActivity.this,
                                    "친구의 위치 데이터를 찾을 수 없습니다.",
                                    Toast.LENGTH_SHORT).show());
                        }
                    });
        } else {
            // 자신이 위치를 공유하는 사용자일 때 (friendUserId와 동일할 때)
            // 자신의 위치를 Firestore에 저장하는 로직을 수행
            getLastKnownLocation();  // 자신의 위치를 Firestore에 저장하는 메서드 호출
        }
    }


    private void drawFriendLocationOnMap(LatLng friendLocation) {
        if (googleMap != null) {
            // 기존에 다른 마커를 지우지 않도록 googleMap.clear()는 사용하지 않음
            if (friendMarker != null&&friendLocation != null) {
                friendMarker.remove();
            }
            // 새로운 마커 추가
            friendMarker = googleMap.addMarker(new MarkerOptions()
                    .position(friendLocation)
                    .title("친구의 현재 위치"));

            // 카메라 이동 시 기존 줌 레벨 유지
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(friendLocation, currentZoomLevel));
        } else {
            Toast.makeText(HomeActivity.this, "지도 준비가 완료되지 않았습니다.", Toast.LENGTH_SHORT).show();
        }
    }



    private void updateCurrentLocationMarker(LatLng currentLocation) {
        if (currentMarker != null) {
            // 기존 마커의 위치만 업데이트
            currentMarker.setPosition(currentLocation);
        } else {
            // 마커가 없으면 새로 추가
            currentMarker = googleMap.addMarker(new MarkerOptions()
                    .position(currentLocation)
                    .title("내 위치")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));  // 마커 스타일 지정 가능
        }
    }


    // Firestore에서 친구 이름을 사용해 UID를 찾는 메서드 (새로 추가)
    private void getFriendUserIdByName(String friendName, OnSuccessListener<String> onSuccessListener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Firestore에서 친구 이름으로 UID 검색
        db.collection("users")
                .whereEqualTo("name", friendName)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // 첫 번째 문서에서 UID 가져오기
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        String friendUserId = documentSnapshot.getId(); // UID는 문서 ID
                        onSuccessListener.onSuccess(friendUserId);
                    } else {
                        // 친구 이름을 가진 문서를 찾지 못함
                        Toast.makeText(HomeActivity.this, "친구의 UID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Firestore 요청 실패 시
                    Toast.makeText(HomeActivity.this, "UID를 검색하는 중 오류가 발생했습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // 콜백 인터페이스 정의 (비동기 작업을 처리하기 위해)
    private interface FirestoreCallback {
        void onCallback(String userId);
    }


    @Override
    public void onBackPressed() {
        if (!isDialogShowing) { // 다이얼로그가 열려 있지 않을 때만 다이얼로그 표시
            // 다이얼로그 생성
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_exit_confirmation, null);
            builder.setView(dialogView);

            Button btnYes = dialogView.findViewById(R.id.btn_yes);
            Button btnNo = dialogView.findViewById(R.id.btn_no);

            // 다이얼로그 생성
            AlertDialog dialog = builder.create();
            isDialogShowing = true; // 다이얼로그 상태 변경

            // "예" 버튼 클릭 시 종료
            btnYes.setOnClickListener(v -> {
                dialog.dismiss(); // 다이얼로그 닫기
                exit(); // 앱 종료
                isDialogShowing = false; // 다이얼로그 상태 초기화
            });

            // "아니요" 버튼 클릭 시 다이얼로그 닫기
            btnNo.setOnClickListener(v -> {
                dialog.dismiss();
                isDialogShowing = false; // 다이얼로그 상태 초기화
            });

            // 다이얼로그 보이기
            dialog.show();
        } else {
            super.onBackPressed(); // 다이얼로그가 열려 있으면 기본 동작 수행
        }
    }

    public void exit() {
        finish(); // 현재 액티비티 종료
    }

    private void handleReturnButtonClick() {
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        String userName = "사용자"; // 실제 사용자 이름으로 수정

        try {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    // Geocoder 객체 생성
                    Geocoder geocoder = new Geocoder(HomeActivity.this, Locale.getDefault());
                    List<Address> addresses;
                    try {
                        // 주소 가져오기
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        String address  = addresses != null && !addresses.isEmpty() ? addresses.get(0).getAddressLine(0) : "주소를 찾을 수 없습니다.";

                        // 메시지 작성
                        String message = userName + " 님이 " + address  + "에서 출발하였습니다.";
                        Log.d("MainActivity", "Current Address: " + address );
                        Log.d("MainActivity", "Message to save: " + message);  // 로그 추가

                        // SharedPreferences에 데이터 저장
                        SharedPreferences sharedPref = getSharedPreferences("notiData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("message", message); // 직접 문자열 저장
                        editor.putString("currentDateTime", currentDateTime);
                        editor.apply();

                        Log.d("MainActivity", "Data saved to SharedPreferences: " + message);
                    } catch (IOException e) {
                        Log.e("MainActivity", "Geocoder service is unavailable: " + e.getMessage());
                    }
                } else {
                    Log.d("MainActivity", "Location is null");
                }

            }).addOnFailureListener(e -> {
                Log.e("MainActivity", "Failed to get location: " + e.getMessage());
                Toast.makeText(this, "위치 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            });
        } catch (SecurityException e) {
            Log.e("MainActivity", "Location permission not granted: " + e.getMessage());
            Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    public interface LatLngCallback {
        void onLocationRetrieved(LatLng currentLocation);
    }

    private void getCurrentLocation(final LatLngCallback callback) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    callback.onLocationRetrieved(new LatLng(location.getLatitude(), location.getLongitude()));
                } else {
                    callback.onLocationRetrieved(null);
                }
            }).addOnFailureListener(e -> {
                callback.onLocationRetrieved(null);
                Toast.makeText(this, "위치 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show();
            });
        } else {
            callback.onLocationRetrieved(null);
            requestLocationPermissions();
        }
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        }, LOCATION_REQUEST_REQUEST_CODE);
    }

    private void selectRoute() {
        getCurrentLocation(new LatLngCallback() {
            @Override
            public void onLocationRetrieved(LatLng currentLocation) {
                String locationAddress = currentLocation != null ? getAddressFromLatLng(currentLocation) : "위치 정보 없음";
                String message = userName + "님이 " + locationAddress + "에서 출발하였습니다.";
                addNotification(message);
            }
        });
    }

    // 지도 준비가 완료되었을 때 호출
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Check location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permissions if not already granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                // 사용자가 카메라를 움직일 때 현재 줌 레벨을 저장
                currentZoomLevel = googleMap.getCameraPosition().zoom;
            }
        });

        moveCameraToCurrentLocation();

        // 실시간 위치 업데이트 시작
        startLocationUpdates();
    }

    // 위치 데이터를 Firestore에 저장하는 메서드
    // 위치 공유하는 사용자의 UID를 사용해 위치를 Firestore에 저장
    private void saveLocationDataToFirestore(String userId, double latitude, double longitude) {
        Map<String, Object> locationData = new HashMap<>();
        locationData.put("latitude", latitude);
        locationData.put("longitude", longitude);

        // 위치 데이터를 저장할 때 토스트 메시지 출력
        //Toast.makeText(HomeActivity.this, "저장할userID : "+userId+"저장할 위치 데이터: Lat = " + latitude + ", Lng = " + longitude, Toast.LENGTH_LONG).show();

        db.collection("locations").document(userId)
                .set(locationData)
                .addOnSuccessListener(aVoid -> {
                    // Firestore에 데이터가 저장되었을 때
                    // Toast.makeText(HomeActivity.this, "위치 데이터가 Firestore에 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Firestore에 데이터 저장이 실패했을 때
                    Toast.makeText(HomeActivity.this, "위치 데이터를 저장하는 데 실패했습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    // 친구 위치 실시간 구독 메서드 추가 위치
    private void subscribeToFriendLocation(String friendUserId) {
        db.collection("locations")
                .document(friendUserId)
                .addSnapshotListener((snapshot, e) -> {
                    if (snapshot != null && snapshot.exists()) {
                        // 위치 데이터를 성공적으로 받아온 경우
                        Double latitude = snapshot.getDouble("latitude");
                        Double longitude = snapshot.getDouble("longitude");

                        if (latitude != null && longitude != null) {
                            runOnUiThread(() -> Toast.makeText(HomeActivity.this,
                                    "받아온 위치 데이터: " + latitude + ", " + longitude,
                                    Toast.LENGTH_LONG).show());
                            LatLng friendLocation = new LatLng(latitude, longitude);
                            drawFriendLocationOnMap(friendLocation);  // 친구의 위치를 지도에 표시

                        } else {
                            Log.e("Firestore", "위치 데이터가 올바르지 않습니다.");
                            Toast.makeText(HomeActivity.this, "위치 데이터가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("Firestore", "문서가 존재하지 않습니다.");
                        Toast.makeText(HomeActivity.this, "위치 데이터를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }












    // 실시간 위치 업데이트 및 카메라 이동
    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없으면 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    // moveCameraToCurrentLocation 메서드 시작(현재 위치로 카메라 이동)
    private void moveCameraToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한이 없으면 권한 요청
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // 현재 위치 가져와서 LatLng 객체 생성
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    // 현재 위치로 카메라 이동 및 줌 설정
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                    Toast.makeText(HomeActivity.this, "현재 위치로 이동", Toast.LENGTH_SHORT).show();

                    if(currentMarker != null){
                        currentMarker.remove();
                    }
                    currentMarker = googleMap.addMarker(new MarkerOptions().position(currentLocation).title("현재 위치"));
                } else {
                    Toast.makeText(HomeActivity.this, "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // moveCameraToCurrentLocation 메서드 끝

    // showBottomSheetDialog 코드 시작
    private void showBottomSheetDialog() {
        // BottomSheetDialog 생성
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomeActivity.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);

        // 공유 버튼 클릭 이벤트 처리
        Button shareButton = bottomSheetView.findViewById(R.id.share_button);
        RadioGroup radioGroup = bottomSheetView.findViewById(R.id.friend_radio_group);

        shareButton.setOnClickListener(view -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                String selectedFriend = ((RadioButton) bottomSheetView.findViewById(selectedId)).getText().toString();

                getFriendUserIdByName(selectedFriend, friendUserId -> {
                    if (friendUserId != null && !friendUserId.isEmpty()) {
                        // 현재 위치 가져오기
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                            return;
                        }

                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                            if (location != null) {
                                // Firestore에 위치 저장
                                saveLocationDataToFirestore("xrS5akNYLLOWANp4okJVHZqH6k22", location.getLatitude(), location.getLongitude());

                                // Firestore 저장 후 경로 요청
                                getDirections(new LatLng(location.getLatitude(), location.getLongitude()), selectedLocation);

                                // 위치 공유 화면 닫기
                                bottomSheetDialog.dismiss();

                                Toast.makeText(HomeActivity.this, selectedFriend + "에게 위치를 공유합니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomeActivity.this, "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(HomeActivity.this, "위치를 가져오는 데 실패했습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        Toast.makeText(HomeActivity.this, "친구의 UID를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(HomeActivity.this, "친구를 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    // showBottomSheetDialog 코드 끝




    // getCurrentLocationAndShowDirections 메서드 코드 시작(현재 위치를 가져와 검색된 목적지까지의 경로를 표시하는 메서드)
    private void getCurrentLocationAndShowDirections() {
        // 현재 위치로부터 경로 요청
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // 권한 요청 처리
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return;
        }

        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    getDirections(currentLocation, selectedLocation); // 경로 요청
                } else {
                    Toast.makeText(HomeActivity.this, "현재 위치를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // getCurrentLocationAndShowDirections 메서드 코드 끝


    // getDirections 메서드 코드 시작(도보 및 대중교통 경로 예상 시간 가져오기)
    private void getDirections(LatLng origin, LatLng destination) {
        String apiKey = DIRECTIONS_API_KEY;
        String url = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origin.latitude + "," + origin.longitude
                + "&destination=" + destination.latitude + "," + destination.longitude
                + "&alternatives=true"  // 여러 경로 요청
                + "&mode=transit"       // 대중교통 경로 요청
                + "&key=" + apiKey;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(HomeActivity.this, "경로를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseData);
                        JSONArray routes = jsonResponse.getJSONArray("routes");

                        if (routes.length() > 0) {
                            runOnUiThread(() -> showRouteSelectionDialog(routes));


                        }
                    } catch (JSONException e) {
                        runOnUiThread(() -> Toast.makeText(HomeActivity.this, "경로를 분석하는 데 실패했습니다.", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });
    }


    // getDirections 메서드 코드 끝



    // showRouteSelectionDialog 메서드 (경로를 선택할 수 있는 BottomSheetDialog를 사용)


    // 기존 showRouteSelectionDialog 메서드에 실시간 시간 감소 타이머 추가
    private void showRouteSelectionDialog(JSONArray routes) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(HomeActivity.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_route_selection, null);

        RecyclerView recyclerView = bottomSheetView.findViewById(R.id.route_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RouteAdapter adapter = new RouteAdapter(routes, new RouteAdapter.OnRouteSelectedListener() {
            @Override
            public void onRouteSelected(int position, String duration) {
                try {
                    // 경로를 선택 했을 때 실행되는 기능들
                    JSONObject selectedRoute = routes.getJSONObject(position);
                    String points = selectedRoute.getJSONObject("overview_polyline").getString("points");

                    // start_button_imageView를 pause_button_imageView로 변경
                    ImageView pauseButtonImageView = findViewById(R.id.pause_button_imageView);
                    pauseButtonImageView.setVisibility(View.VISIBLE);

                    // start_button_imageView는 숨기기
                    ImageView startButtonImageView = findViewById(R.id.start_button_imageView);
                    startButtonImageView.setVisibility(View.GONE);

                    // buttonContainer를 숨김
                    LinearLayout buttonContainer = findViewById(R.id.button_container);
                    buttonContainer.setVisibility(View.GONE);

                    // 선택된 경로 그리기
                    drawPolyline(points);

                    // 예상 시간과 목적지 좌표 가져오기
                    JSONObject leg = selectedRoute.getJSONArray("legs").getJSONObject(0);
                    String durationText = leg.getJSONObject("duration").getString("text"); // 예상 시간 추출
                    JSONObject endLocation = leg.getJSONObject("end_location");
                    selectedLocation = new LatLng(endLocation.getDouble("lat"), endLocation.getDouble("lng"));

                    // 경로가 선택되었음을 표시
                    isRouteSelected = true; // 이제 부터 거리 비교 가능

                    // 예상 시간을 변환하여 분 단위로 저장
                    remainingMinutes = parseDurationToMinutes(durationText);

                    // 예상 시간을 TextView에 설정하고 표시
                    String formattedTime = formatRemainingTime(remainingMinutes);
                    estimatedTimeTextView.setText(formattedTime);
                    estimatedTimeTextView.setVisibility(View.VISIBLE);

                    // 타이머 시작
                    startCountdownTimer();

                    // 목적지 반경 30m원 그리기
                    drawDestinationProximityCircle(selectedLocation);


                    // 경로 선택 후 카메라 이동 및 시작버튼, 경로 등 설정
                    bottomSheetDialog.dismiss();
                    handleReturnButtonClick();

                    // 경로 선택 후 카메라 이동 및 시작버튼, 경로 등 설정
                    bottomSheetDialog.dismiss();
                    handleReturnButtonClick();


                    // 출발 알림 업로드
                    String message  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                    if (currentLocation != null) {
                        String currentAddress = getAddressFromLatLng(currentLocation); // currentLocation을 주소로 변환
                        uploadNotification(message, userName + " 님이 " + currentAddress + "에서 출발하였습니다." + "\n");
                    } else {
                        uploadNotification(message, userName + " 님의 현재 위치를 찾을 수 없습니다.\n");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView.setAdapter(adapter);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

        // 기본적으로 stop_button_imageView와 buttonContainer는 보이지 않도록 설정
        ImageView stopButtonImageView = findViewById(R.id.stop_button_imageView);
        stopButtonImageView.setVisibility(View.GONE);
        LinearLayout buttonContainer = findViewById(R.id.button_container);
        buttonContainer.setVisibility(View.GONE);
    }

    private void updateCurrentLocation(LatLng newLocation) {
        currentLocation = newLocation; // 현재 위치 업데이트
    }


    /* 시간이 흐르는 메서드로 변경 했기 때문에 사용 X
private String formatDuration(String durationText) {
    // "1 hour 30 mins" 또는 "30 mins"와 같은 형식의 문자열을 처리
    int hours = 0;
    int minutes = 0;

    // "hour"와 "mins"를 기준으로 문자열을 분리
    if (durationText.contains("hour")) {
        String[] hourParts = durationText.split("hour");
        hours = Integer.parseInt(hourParts[0].trim());

        if (hourParts.length > 1 && hourParts[1].contains("mins")) {
            minutes = Integer.parseInt(hourParts[1].replace("mins", "").trim());
        }
    } else if (durationText.contains("mins")) {
        minutes = Integer.parseInt(durationText.replace("mins", "").trim());
    }

    // "남은 시간 : X시간 Y분" 형식으로 변환
    if (hours > 0 && minutes > 0) {
        return "남은 시간 : " + hours + "시간 " + minutes + "분";
    } else if (hours > 0) {
        return "남은 시간 : " + hours + "시간";
    } else {
        return "남은 시간 : " + minutes + "분";
    }



    */


    // drawPolyline 메서드 코드 시작
    private void drawPolyline(String encodedPath) {
        List<LatLng> decodedPath = PolyUtil.decode(encodedPath);

        // 현재 그려진 경로 실패
        if(currentPolyline != null){
            currentPolyline.remove();
        }


        // 새 경로 그리기 및 저장
        currentPolyline = googleMap.addPolyline(new PolylineOptions()
                .addAll(decodedPath)
                .width(10)
                .color(Color.BLUE));
    }
    // drawPolyline 메서드 코드 끝



    // 목적지 마커 표시 메서드
    private void addDestinationMarker(LatLng destination, String title) {
        // 기존 목적지 마커가 있으면 제거
        if (destinationMarker != null) {
            destinationMarker.remove();
        }

        // 새 목적지 마커 추가
        destinationMarker = googleMap.addMarker(new MarkerOptions()
                .position(destination)
                .title(title));
    }


    // 경로지우기 메서드
    private void clearPolyline(){
        if (currentPolyline != null){
            currentPolyline.remove();
            currentPolyline = null;
        }
    }

    // 목적지 핑 지우기 메서드
    private void clearDestinationMarker() {
        if (destinationMarker != null) {
            destinationMarker.remove();
            destinationMarker = null;
        }
    }


    // 사용자의 현재 위치를 가져오는 메서드
/* 현재위치를 가져오는 메서드이며 지금은 실시간으로 가져오기 때문에 사용 X
private void getCurrentLocation() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return;
    }
    Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
    locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
        @Override
        public void onSuccess(Location location) {
            if (location != null) {
                // Get the current location and move the camera
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                // Add a marker on the current location
                googleMap.addMarker(new MarkerOptions().position(currentLocation).title("현재 위치"));
            }
        }
    });
}
*/


    // Handle the result from the Place Autocomplete search
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && resultCode == RESULT_OK) {
            Place place = Autocomplete.getPlaceFromIntent(data);
            this.selectedLocation = place.getLatLng(); // 검색된 자표 저장


            // Move the camera to the selected location and add a marker
            if(selectedLocation != null){
                if (destinationMarker != null) {
                    destinationMarker.remove();  // 기존 마커 제거
                }

                destinationMarker = googleMap.addMarker(new MarkerOptions()
                        .position(selectedLocation)
                        .title(place.getName()));

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15));
            }else {
                Toast.makeText(this, "목적지를 가져오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        }else if (resultCode == AutocompleteActivity.RESULT_ERROR && data != null) {
            // Autocomplete 검색에서 오류 발생 시
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, "검색 오류: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Handle location permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation(); // 권한이 허용되면 현재 위치 가져오기
            } else {
                // 권한 거부 시 처리할 로직
                Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // 예상 시간을 실시간으로 줄이는 타이머 시작 메서드 추가
    private void startCountdownTimer() {
        updateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                if (remainingMinutes > 0) {
                    remainingMinutes--; // 1분씩 감소

                    // 시간을 "1시간" 또는 "59분" 등의 형식으로 표시
                    String formattedTime = formatRemainingTime(remainingMinutes);
                    estimatedTimeTextView.setText(formattedTime);

                    handler.postDelayed(this, 60000); // 1분 후 다시 실행
                }else{
                    showTimeUpDialog();
                }
            }
        };
        //타이머 시작
        handler.postDelayed(updateTimeRunnable, 60000); // 1분 후 시작
    }


    // 남은 시간을 "1시간" 또는 "59분"으로 표시하는 메서드
    private String formatRemainingTime(int minutes) {
        if (minutes >= 60) {
            int hours = minutes / 60;
            int remainingMinutes = minutes % 60;

            if (remainingMinutes > 0) {
                return "예상 남2aks은 시간 : " + hours + "시간 " + remainingMinutes + "분";
            } else {
                return "예상 남은 시간 : " + hours + "시간";
            }
        } else {
            return "예상 남은 시간 : " + minutes + "분";
        }
    }


    // durationText를 분으로 변환하는 메서드 추가
    private int parseDurationToMinutes(String durationText) {
        int hours = 0;
        int minutes = 0;

        // "hour"와 "mins"를 기준으로 문자열을 분리
        if (durationText.contains("hour")) {
            String[] hourParts = durationText.split("hour");

            // 숫자로 변환 가능한지 체크
            try {
                hours = Integer.parseInt(hourParts[0].trim().replaceAll("[^\\d]", "")); // 숫자만 남기고 변환
            } catch (NumberFormatException e) {
                Log.e("MainActivity", "Error parsing hours: " + e.getMessage());
            }

            if (hourParts.length > 1 && hourParts[1].contains("mins")) {
                try {
                    minutes = Integer.parseInt(hourParts[1].replace("mins", "").trim().replaceAll("[^\\d]", "")); // 숫자만 남기고 변환
                } catch (NumberFormatException e) {
                    Log.e("MainActivity", "Error parsing minutes: " + e.getMessage());
                }
            }
        } else if (durationText.contains("mins")) {
            try {
                minutes = Integer.parseInt(durationText.replace("mins", "").trim().replaceAll("[^\\d]", "")); // 숫자만 남기고 변환
            } catch (NumberFormatException e) {
                Log.e("MainActivity", "Error parsing minutes: " + e.getMessage());
            }
        }

        return hours * 60 + minutes; // 분 단위로 변환하여 반환
    }



    private void stopCountdownTimer(){
        handler.removeCallbacks(updateTimeRunnable);
    }


    private void showTimeUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle("시간이 다 되었습니다!");

        // 팝업에 들어갈 "10분 추가" 버튼과 "도착" 버튼 추가
        builder.setMessage("어떻게 하시겠습니까?")
                .setPositiveButton("10분 추가", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        remainingMinutes += 10; // 10분 추가
                        String formattedTime = formatRemainingTime(remainingMinutes);
                        estimatedTimeTextView.setText(formattedTime);
                        startCountdownTimer(); // 타이머 다시 시작
                    }
                })
                .setNegativeButton("도착", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(HomeActivity.this, "도착했습니다!", Toast.LENGTH_SHORT).show();
                        stopCountdownTimer(); // 타이머 중지
                        // 경로 지우기
                        clearPolyline();

                        // 목적지 핑 지우기
                        clearDestinationMarker();

                        // 목적지 좌표 초기화
                        selectedLocation = null;

                        // 목적지 원 지우기
                        clearDestinationProximityCircle();


                        // 남은 시간 텍스트 숨기기
                        estimatedTimeTextView.setVisibility(View.GONE);

                        stopButtonImageView.setVisibility(View.GONE);
                        pauseButtonImageView.setVisibility(View.GONE); // 처음에 pause 버튼도 숨기기
                        buttonContainer.setVisibility(View.GONE);
                        startButtonImageView.setVisibility(View.VISIBLE);


                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    // 목적지 반경 30미터 안에 들어왔을 때 메소드
    private void checkProximityToDestination(LatLng currentLocation, LatLng selectedLocation) {
        if (selectedLocation != null) {
            float[] results = new float[1];
            Location.distanceBetween(
                    currentLocation.latitude, currentLocation.longitude,
                    selectedLocation.latitude, selectedLocation.longitude,
                    results
            );

            // 100미터 이내일 때 토스트 메시지 출력
            if (results[0] < 100) {

                // 알림 메시지 생성
                String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                String notificationMessage = "사용자 님이 목적지로부터 반경 100m 안에 진입하였습니다." + "\n";
                // 알림창에 업로드하는 코드 추가
                uploadNotification(currentDateTime, notificationMessage);

                Toast.makeText(HomeActivity.this, "목적지 반경 100m 안에 들어왔습니다!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    // 알림창에 업로드하는 메서드
    private void uploadNotification(String dateTime, String message) {
        if (notificationList.size() >= 30) {
            notificationList.remove(notificationList.size() - 1); // 가장 오래된 알림 제거
        }

        NotificationItem newNotification = new NotificationItem(dateTime, message);
        notificationList.add(0, newNotification); // 새 알림을 맨 위에 추가
        adapter.notifyItemInserted(0); // 어댑터에 새 항목 알림

        Log.d("Notification", "업로드된 메시지: " + message);

        // SharedPreferences에 저장
        saveNotificationToPreferences(dateTime, message);
    }

    private void drawDestinationProximityCircle(LatLng destinationLocation) {
        if (destinationLocation != null) {
            // 기존에 그려진 원이 있으면 제거
            if (proximityCircle != null) {
                proximityCircle.remove();
            }

            // CircleOptions 객체 생성
            CircleOptions circle30M = new CircleOptions()
                    .center(destinationLocation)       // 원점: 목적지 좌표
                    .radius(100)                        // 반경 100미터
                    .strokeWidth(0f)                   // 선너비 0f: 선 없음
                    .fillColor(Color.parseColor("#880000ff"));  // 반경 원 배경색 (파란색, 투명도 50%)

            // 지도에 원을 추가하고 Circle 객체 저장
            proximityCircle = googleMap.addCircle(circle30M);
        }
    }


    private void clearDestinationProximityCircle() {
        if (proximityCircle != null) {
            proximityCircle.remove();  // 지도에서 원 제거
            proximityCircle = null;    // 객체 초기화
        }
    }

    // 위치 좌표(LatLng)를 주소로 변환하는 메서드
    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0); // 주소 라인 가져오기
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "주소를 찾을 수 없습니다."; // 주소를 찾지 못했을 때 반환할 기본 메시지
    }

    // 현재 위치를 비동기로 가져오는 메서드
    private void getCurrentLocation() {

        // 위치 업데이트 요청
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000); // 5초마다 위치 요청

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        LatLng newLocation = new LatLng(latitude, longitude);
                        updateCurrentLocation(newLocation);

                        // Firestore에 위치 저장
                        saveLocationDataToFirestore(friendUserId, location.getLatitude(), location.getLongitude());
                    }
                });
    }


    // 알림 추가 메서드
    private void addNotification(String message) {
        // 현재 날짜와 시간 가져오기
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());

        if (notificationList.size() >= 30) {
            notificationList.remove(29); // 가장 오래된 기록 삭제
        }

        // 새 알림 추가
        notificationList.add(0, new NotificationItem(currentDateTime, message));
        notificationAdapter.notifyItemInserted(0);

        // SharedPreferences에 저장
        saveNotificationToPreferences(currentDateTime, message);
    }

    // SharedPreferences에 알림 저장
    private void saveNotificationToPreferences(String dateTime, String message) {
        SharedPreferences sharedPref = getSharedPreferences("notiData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // 알림을 JSON 형식으로 저장
        String json = sharedPref.getString("notifications", "[]");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<NotificationItem>>() {}.getType();
        ArrayList<NotificationItem> notifications = gson.fromJson(json, type);

        if (notifications.size() >= 30) {
            notifications.remove(notifications.size() - 1); // 가장 오래된 기록 삭제
        }

        notifications.add(0, new NotificationItem(dateTime, message)); // 새 알림 추가
        String newJson = gson.toJson(notifications);
        editor.putString("notifications", newJson);
        editor.apply();
    }





    // MapView 라이프사이클 메서드 추가
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}


