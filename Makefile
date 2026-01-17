.PHONY: clean compile run

clean:
	rm -rf **/target **/project/target
	rm -rf .bsp .metals .bloop
	rm -rf impl/.bsp output_directory/.bsp
	rm -f **/.history* **/sbt.json

compile:
	cd output_directory && sbt clean compile
	cd impl && sbt clean compile

run:
	cd impl && sbt run

test:
	curl -i http://localhost:8080/teams
	curl -i http://localhost:8080/players
	curl -i http://localhost:8080/matches
