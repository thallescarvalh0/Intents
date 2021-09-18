package br.edu.ifsp.scl.sdm.pa1.intents;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import br.edu.ifsp.scl.sdm.pa1.intents.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private ActivityResultLauncher<String> requisicaoPermissoesActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.mainTb.appTb.setSubtitle("Principais tipos");
        activityMainBinding.mainTb.appTb.setTitle("Tratando Intents");

        setSupportActionBar(activityMainBinding.mainTb.appTb);

        requisicaoPermissoesActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                concedida -> {
                    if (!concedida){
                        requisitarPermissaoLigacao();
                    }
                    else{
                        discarTelefone();
                    }
                }
        );

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.viewMi:{
                String url = activityMainBinding.parameterEt.getText().toString();
                String http = "http://";
                if (!url.contains("https")){
                    url = http + url;
                }
                Intent siteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(siteIntent);
                return true;
            }
            case R.id.callMi:{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        requisitarPermissaoLigacao();
                    }
                    else{
                        discarTelefone();
                    }
                }else{
                    discarTelefone();
                }
                return true;
            }
            case R.id.dialMi:{
                Intent ligacaoIntent = new Intent(Intent.ACTION_DIAL);
                ligacaoIntent.setData(Uri.parse("tel: "+activityMainBinding.parameterEt.getText().toString()));
                startActivity(ligacaoIntent);
                return true;
            }
            case R.id.pickMi:
            case R.id.chooserMi: {
                return true;
            }
            case R.id.exitMi:{
                finish();
                return true;
            }
            case R.id.actionMi:{
                Intent actionIntent = new Intent("OPEN_ACTION_ACTIVITY").putExtra(
                        Intent.EXTRA_TEXT,
                        activityMainBinding.parameterEt.getText().toString()
                );
                startActivity(actionIntent);
                return true;
            }

            default:{
                return false;
            }
        }
    }

    private void discarTelefone() {
        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel: "+activityMainBinding.parameterEt.getText().toString())));
    }
    private void requisitarPermissaoLigacao() {
        requisicaoPermissoesActivityResultLauncher.launch(Manifest.permission.CALL_PHONE);
    }
}