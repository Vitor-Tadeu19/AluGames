package org.example

import com.google.gson.Gson
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpResponse.BodyHandlers
import java.util.Scanner


fun main() {
    val leitura = Scanner(System.`in`)
    println("Digite um código de jogo para buscar: ")
    val busca = leitura.nextLine()

    val endereco = "https://www.cheapshark.com/api/1.0/games?id=$busca"

    val client: HttpClient = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder()
        .uri(URI.create(endereco))
        .build()
    val response: HttpResponse<String> = client
        .send(request, BodyHandlers.ofString())

    val json = response.body()

    val gson = Gson()

    var meuJogo: Jogo? = null

//runCatching substitui o try catch
    val resultado = runCatching {
        val meuInfoJogo = gson.fromJson(json, InfoJogo::class.java)

        meuJogo = Jogo(meuInfoJogo.info.title, meuInfoJogo.info.thumb)

    }

    resultado.onFailure {
        println("Jogo inexistente, digite um código de jogo válido.")
    }

    resultado.onSuccess {
        println("Deseja inserir uma descrição personalizada? S/N")
        val opcao = leitura.nextLine()

        if (opcao.equals("s", true)){
            println("Insira a descrição personalizada: ")
            val descricaoPersonalizada = leitura.nextLine()
            meuJogo?.descricao = descricaoPersonalizada
            println(meuJogo)
        }else {
            println("Ok! A descrição do jogo será igual ao título.")
            meuJogo?.descricao = meuJogo.titulo
            println(meuJogo)
        }

        resultado.onSuccess {
            println("Busca finalizada com sucesso!")
        }
    }
}