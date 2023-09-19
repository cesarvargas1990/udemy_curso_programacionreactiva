package com.bolsadeideas.springboot.reactor.app;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
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
		//ejemploZipWithRangos();
		//ejemploInterval();
		//ejemploDelayElements();
		//ejemploIntervaloInfinito();
		//ejemploIntervaloDesdeCreate();
		ejemploContraPresion2();
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
	
	public void ejemploInterval() {
		Flux<Integer> rango = Flux.range(1, 12);
		Flux<Long> retraso = Flux.interval(Duration.ofSeconds(1));
		
		rango.zipWith(retraso, (ra, re) -> ra)
		.doOnNext(i-> Log.info(i.toString()))
		.blockLast();
	}
	
	public void ejemploDelayElements() throws InterruptedException {
		Flux<Integer> rango = Flux.range(1, 12)
				.delayElements(Duration.ofSeconds(1))
				.doOnNext(i->Log.info(i.toString()))
				;
		
		rango.blockLast(); // mejor usar este
		rango.subscribe();
		Thread.sleep(13000);
		//rango.zipWith(retraso, (ra, re) -> ra)
		//.doOnNext(i-> Log.info(i.toString()))
		//.blockLast();
	}
	
	public void ejemploIntervaloInfinito() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		
		Flux.interval(Duration.ofSeconds(1))
		.doOnTerminate(() -> latch.countDown())
		.flatMap(i-> {
			if (i>=5) {
				return Flux.error(new InterruptedException("Solo hasta 5!!"));
			}
			return Flux.just(i);
		})
		.map(i->"hola" + i)
		.retry(2)
		.doOnNext(s->Log.info(s))
		.subscribe(s->Log.info(s), e->Log.error(e.getMessage()));
		
		latch.await();
	}
	
	public void ejemploIntervaloDesdeCreate() throws InterruptedException {
		Flux.create(emmitter -> {
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				private Integer contador = 0;

				@Override
				public void run() {
					emmitter.next(contador++);
					if (contador == 10) {
						emmitter.complete();
					}
					if (contador == 5) {
						timer.cancel();
						emmitter.error(new InterruptedException("Error, se ha detenido el flux en 5!"));
					}

				}

			}, 1000, 1000);
		}).subscribe(next -> Log.info(next.toString()), error -> Log.error(error.getMessage()),
				() -> Log.info("Hemos Terminado!!"));
	}
	
	public void ejemploContraPresion() {
		Flux.range(1, 10)
		.log()
		.subscribe(  new Subscriber<Integer>() {
			
			private Subscription s;
			private Integer limit = 5;
			private Integer consume = 0;
			@Override
			public void onSubscribe(Subscription s) {
				this.s = s;
				s.request(limit);
				
			}

			@Override
			public void onNext(Integer t) {
				Log.info(t.toString());
				consume++;
				if (consume == limit) {
					consume = 0;
					s.request(limit);
				}
				
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onComplete() {
				// TODO Auto-generated method stub
				
			}

			
		}   );
	}
	
	public void ejemploContraPresion2() {
		Flux.range(1, 10)
		.log()
		.limitRate(5)
		.subscribe();
	}

}
