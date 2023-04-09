package edu.esi.uclm.es.ds.games;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ServletComponentScan
@RestController

@RequestMapping("games")

public class LanzadoraGames 
{
    public static void main( String[] args )
    {
    	SpringApplication.run(LanzadoraGames.class, args);
    }
}
