package com.bolsadeideas.springboot.reactor.app;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bolsadeideas.springboot.reactor.app.models.Comentarios;
import com.bolsadeideas.springboot.reactor.app.models.Usuario;
import com.bolsadeideas.springboot.reactor.app.models.UsuarioComentarios;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class SpringBootReactorApplication implements CommandLineRunner {

	private static final Logger Log = LoggerFactory.getLogger(SpringBootReactorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootReactorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		// ejemploIterable();
		// ejemploFlatMap();
		// ejemploToString();
		// ejemploCollectList();
		// ejemploUsuarioComentariosFlatMap();
		//ejemploUsuarioComentariosZipWith();
		//ejemploUsuarioComentariosZipWith2();
		ejemploZipWithRangos();
	}

	public void ejemploIterable() throws Exception {

		List<String> usuarios = new ArrayList<>();
		usuarios.add("cristiano ronaldo");
		usuarios.add("leo messi");
		usuarios.add("adrian ramos");

		Flux<String> nm2 = Flux.fromIterable(usuarios);
		Flux<Usuario> usf = nm2.map(us -> new Usuario(us.split(" ")[0].toUpperCase(), us.split(" ")[1].toUpperCase()));

		usf.subscribe(s -> {
			Log.info("subscribe de usf");
			Log.info(s.getNombre() + " " + s.getApellido());
		});

		// TODO Auto-generated method stub
		Flux<Usuario> nombres = Flux
				.just("cesar vargas", "pedro wills", "juan sutano", "cesar Wills", "cc broll", "bruce wills")
				.map(nombre -> {
					return new Usuario(nombre.split(" ")[0].toUpperCase(), nombre.split(" ")[1].toUpperCase());
				}).filter(f -> f.getNombre().equals("cesar".toUpperCase()))

				.doOnNext(usuario -> {
					if (usuario == null) {
						throw new RuntimeException("Nombres no pueden ser vacios");
					}
					Log.info("dentro de onnext " + usuario.getNombre().concat(" ").concat(usuario.getApellido()));

				});

		nombres.subscribe(e -> Log.info(e.toString()), error -> Log.error(error.getMessage()), new Runnable() {

			@Override
			public void run() {
				Log.info("aqui se ejecuta metodo posterior a ejecucion del observable con exito");

			}
		});
	}

	public void ejemploFlatMap() throws Exception {

		List<String> usuarios = new ArrayList<>();
		usuarios.add("cristiano ronaldo");
		usuarios.add("leo messi");
		usuarios.add("adrian ramos");
		usuarios.add("cesar vargas");
		usuarios.add("arnold swagger");
		usuarios.add("cesar ochoa");
		Flux.fromIterable(usuarios)
				.map(us -> new Usuario(us.split(" ")[0].toUpperCase(), us.split(" ")[1].toUpperCase()))
				.flatMap(usuario -> {

					if (usuario.getNombre().equalsIgnoreCase("cesar")) {
						return Mono.just(usuario);
					} else {
						return Mono.empty();
					}
				})

				.map(usuario -> {
					String nombre = usuario.getNombre().toLowerCase();
					usuario.setNombre(nombre);
					return usuario;
				}).subscribe(s -> Log.info(s.toString()));

	}

	public void ejemploToString() throws Exception {

		List<Usuario> usuarios = new ArrayList<>();
		usuarios.add(new Usuario("cesar", "vargas"));
		usuarios.add(new Usuario("cristiano", "ronaldo"));
		usuarios.add(new Usuario("leo", "messi"));
		usuarios.add(new Usuario("cesar", "boric"));
		usuarios.add(new Usuario("pedro", "sanchez"));

		Flux.fromIterable(usuarios).map(usuario -> usuario.getNombre().concat(" ").concat(usuario.getApellido()))
				.flatMap(nombre -> {

					if (nombre.contains("CESAR".toLowerCase())) {
						return Mono.just(nombre);
					} else {
						return Mono.empty();
					}
				})

				.map(nombre -> {

					return nombre.toLowerCase();
				}).subscribe(s -> Log.info(s.toString()));

	}

	public void ejemploCollectList() throws Exception {

		List<Usuario> usuarios = new ArrayList<>();
		usuarios.add(new Usuario("cesar", "vargas"));
		usuarios.add(new Usuario("cristiano", "ronaldo"));
		usuarios.add(new Usuario("leo", "messi"));
		usuarios.add(new Usuario("cesar", "boric"));
		usuarios.add(new Usuario("pedro", "sanchez"));

		Flux.fromIterable(usuarios).collectList().subscribe(lista -> {
			lista.forEach(item -> {
				Log.info(item.toString());
			});

		});

	}

	public void ejemploUsuarioComentariosFlatMap() {
		Mono<Usuario> usuarioMono = Mono.fromCallable(() -> {
			return new Usuario("jhon", "doe");
		});

		Mono<Comentarios> comentarioUsuarioMono = Mono.fromCallable(() -> {

			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("comentario de prueba");
			comentarios.addComentario("Otro comentario");
			return comentarios;
		});

		usuarioMono.flatMap(u -> comentarioUsuarioMono.map(c -> new UsuarioComentarios(u, c)))
				.subscribe(uc -> Log.info(uc.toString()));

	}

	public void ejemploUsuarioComentariosZipWith() {
		Mono<Usuario> usuarioMono = Mono.fromCallable(() -> {
			return new Usuario("jhon", "doe");
		});

		Mono<Comentarios> comentarioUsuarioMono = Mono.fromCallable(() -> {

			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("comentario de prueba");
			comentarios.addComentario("Otro comentario");
			return comentarios;
		});

		Mono<UsuarioComentarios> usuariosConComentatios = usuarioMono.zipWith(comentarioUsuarioMono,
				(usuario, comentariosUsuario) -> new UsuarioComentarios(usuario, comentariosUsuario));

		usuariosConComentatios.subscribe(uc -> Log.info(uc.toString()));

	}
	

	public void ejemploUsuarioComentariosZipWith2() {
		Mono<Usuario> usuarioMono = Mono.fromCallable(() -> {
			return new Usuario("jhon", "doe");
		});

		Mono<Comentarios> comentarioUsuarioMono = Mono.fromCallable(() -> {

			Comentarios comentarios = new Comentarios();
			comentarios.addComentario("comentario de prueba");
			comentarios.addComentario("Otro comentario");
			return comentarios;
		});

		Mono<UsuarioComentarios> usuariosConComentatios = usuarioMono
				.zipWith(comentarioUsuarioMono)
				.map(tupla -> {
					Usuario u = tupla.getT1();
					Comentarios c = tupla.getT2();
					return new UsuarioComentarios(u,c);
				});

		usuariosConComentatios.subscribe(uc -> Log.info(uc.toString()));

	}
	
	public void ejemploZipWithRangos() {
		Flux.just(1,2,3,4)
		.map( i-> (i*2))
		.zipWith(Flux.range(0, 4), (uno, dos) -> String.format("primer flux %d -- segundo flux %d", uno, dos))
		.subscribe(texto -> Log.info(texto));
		
		
	}

}
