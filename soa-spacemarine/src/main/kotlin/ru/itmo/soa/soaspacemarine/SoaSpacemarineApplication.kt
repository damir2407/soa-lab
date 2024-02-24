package ru.itmo.soa.soaspacemarine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
@RestController
class SoaSpacemarineApplication{
	@GetMapping("/my-health-check")
	fun myCustomCheck(): ResponseEntity<String> {
		val message = "Testing my healh check function"
		return ResponseEntity<String>(message, HttpStatus.OK)
	}
}

fun main(args: Array<String>) {
	runApplication<SoaSpacemarineApplication>(*args)
}
