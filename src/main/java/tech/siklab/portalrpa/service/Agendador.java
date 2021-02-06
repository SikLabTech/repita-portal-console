package tech.siklab.portalrpa.service;

import tech.siklab.portalrpa.model.Cronograma;
import tech.siklab.portalrpa.model.CronogramaExecutado;
import tech.siklab.portalrpa.model.CronogramaExecutando;
import tech.siklab.portalrpa.repository.CronogramaExecutadoRepository;
import tech.siklab.portalrpa.repository.CronogramaExecutandoRepository;
import tech.siklab.portalrpa.repository.CronogramaRepository;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Agendador {

    private String data;
    private String hora;

    @Autowired
    private CronogramaRepository cronogramaRepository;

    @Autowired
    private CronogramaExecutandoRepository cronogramaExecutandoRepository;

    @Autowired
    private CronogramaExecutadoRepository cronogramaExecutadoRepository;

    @Scheduled(fixedRate = 60L * 1000L, initialDelay = 0)
    public void run() {
        data = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        hora = new SimpleDateFormat("HH:mm").format(new Date());

        List<Cronograma> cronogramas = cronogramaRepository.findByHoraInicio(hora);

        for (Cronograma cronograma : cronogramas) {
            if (Objects.nonNull(cronograma.getDataInicio()) && !cronograma.getDataInicio().isEmpty()) {
                if (cronograma.getDataInicio().equals(data)) {

                    gerente(cronograma);
                    if (cronograma.getIntervalo().equals("0")) {
                        cronogramaRepository.delete(cronograma);
                    } else {
                        try {
                            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = (Date) formatter.parse(cronograma.getDataInicio());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            calendar.add(Calendar.DATE, Integer.parseInt(cronograma.getIntervalo()));
                            date = calendar.getTime();
                            cronograma.setDataInicio(formatter.format(date));
                            cronogramaRepository.save(cronograma);
                        } catch (ParseException ex) {
                            Logger.getLogger(Agendador.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } else {
                gerente(cronograma);
            }
        }
    }

    private void gerente(Cronograma cronograma) {

        CronogramaExecutando cronogramaExecutando = new CronogramaExecutando();
        cronogramaExecutando.setDataInicio(data);
        cronogramaExecutando.setHoraInicio(hora);
        cronogramaExecutando.setNome(cronograma.getNome());
        cronogramaExecutando.setReceptor(cronograma.getReceptor());
        cronogramaExecutando.setRobo(cronograma.getRobo());
        cronogramaExecutandoRepository.saveAndFlush(cronogramaExecutando);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.DAYS) // connect timeout
                .writeTimeout(5, TimeUnit.DAYS) // write timeout
                .readTimeout(5, TimeUnit.DAYS) // read timeout
                .build();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", new File(cronograma.getRobo()).getName(),
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                new File(cronograma.getRobo())))
                .build();
        Request request = new Request.Builder()
                .url("http://" + cronograma.getReceptor() + ":38888/exe")
                .method("POST", body)
                .addHeader("Cookie", "JSESSIONID=DB2A06B8DC612136D4E30EA7530862DA")
                .build();
        Response response = null;

        try {
            response = client.newCall(request).execute();
        } catch (IOException ex) {
            Logger.getLogger(Agendador.class.getName()).log(Level.SEVERE, null, ex);
        }

        cronogramaExecutandoRepository.delete(cronogramaExecutando);

        String saida = "";
        try {
            saida = response.body().string();
        } catch (IOException ex) {
            Logger.getLogger(Agendador.class.getName()).log(Level.SEVERE, null, ex);
        }
        CronogramaExecutado cronogramaExecutado = new CronogramaExecutado();
        cronogramaExecutado.setDataInicio(data);
        cronogramaExecutado.setDataTermino(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        cronogramaExecutado.setHoraInicio(hora);
        cronogramaExecutado.setHoraTermino(new SimpleDateFormat("HH:mm").format(new Date()));
        cronogramaExecutado.setMensagem(saida);
        cronogramaExecutado.setNome(cronograma.getNome());
        cronogramaExecutado.setReceptor(cronograma.getReceptor());
        cronogramaExecutado.setRobo(cronograma.getRobo());
        if (!saida.contains("Exception")) {
            cronogramaExecutado.setStatus("Ok");
        } else {
            cronogramaExecutado.setStatus("Erro");
        }
        cronogramaExecutadoRepository.save(cronogramaExecutado);

    }
}
