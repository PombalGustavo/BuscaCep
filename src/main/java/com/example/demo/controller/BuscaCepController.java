package com.example.demo.controller;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BuscaCepController {

    @GetMapping("/")
    public String buscarEndereco() {
        return "index";
    }

    @PostMapping("/buscarEndereco")
    public String buscarCEP(String cep, Model model) {
        try {
            String url = "https://viacep.com.br/ws/" + cep + "/json/";
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) new java.net.URL(url).openConnection();
            connection.setRequestMethod("GET");

            java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            JSONObject json = new JSONObject(response.toString());

            if (json.has("erro")) {
                model.addAttribute("erro", "CEP não encontrado!");
                return "index";
            }

            model.addAttribute("logradouro", json.getString("logradouro"));
            model.addAttribute("bairro", json.getString("bairro"));
            model.addAttribute("cidade", json.getString("localidade"));
            model.addAttribute("estado", json.getString("uf"));


        } catch (java.io.IOException e) {
            model.addAttribute("erro", "Erro de conexão: " + e.getMessage());
        } catch (Exception ex) {
            model.addAttribute("erro", "Erro ao processar o CEP: " + ex.getMessage());
        }
        return "index";
    }

}

