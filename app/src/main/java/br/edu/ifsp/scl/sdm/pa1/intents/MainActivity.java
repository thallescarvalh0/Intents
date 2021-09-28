package br.edu.ifsp.scl.sdm.pa1.intents;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import br.edu.ifsp.scl.sdm.pa1.intents.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;
    private ActivityResultLauncher<String> requisicaoPermissoesActivityResultLauncher;
    private ActivityResultLauncher<Intent> selecionarImagemActivityResultLauncher;
    private ActivityResultLauncher<Intent> escolharAplicativoActivityResultLauncher;


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
        selecionarImagemActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), resultado ->{
                    visualizarImagem(resultado);
                });

        escolharAplicativoActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), resultado ->{
                    visualizarImagem(resultado);
                });

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
            case R.id.pickMi: {
                selecionarImagemActivityResultLauncher.launch(
                        prepararPegarImagemIntent());
            }
            case R.id.chooserMi: {
                Intent escolherAplicativoIntent = new Intent(
                        Intent.ACTION_CHOOSER);
                escolherAplicativoIntent.putExtra(Intent.EXTRA_TITLE, "Escolha um aplicativo para imagens");
                escolherAplicativoIntent.putExtra(Intent.EXTRA_INTENT, prepararPegarImagemIntent());
                escolharAplicativoActivityResultLauncher.launch(escolherAplicativoIntent);

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

    private void visualizarImagem(ActivityResult resultado){
        if (resultado.getResultCode() == RESULT_OK){
            String referenciaImagemUri = resultado.getData().getDataString();
            Intent visualizarImagemIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(referenciaImagemUri));
            startActivity(visualizarImagemIntent);
        }
    }

    private Intent prepararPegarImagemIntent(){
        Intent pegarImagemIntent = new Intent(Intent.ACTION_PICK);
        String diretorio = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
        pegarImagemIntent.setDataAndType(Uri.parse(diretorio), "image/*");
        return pegarImagemIntent;
    }

}