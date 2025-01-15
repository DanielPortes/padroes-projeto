#!/bin/bash

echo "Digite o nome do exercício ou pasta (exemplo: exercicio01): "
read EXERCICIO

DEST=../$EXERCICIO-repo

# diretório temporário
mkdir -p $DEST/src/main/java/padroescriacao
mkdir -p $DEST/src/test/java/padroescriacao

# Copiar pastas
cp .gitignore $DEST/
cp pom.xml $DEST/
cp README.md $DEST/
cp ./diagramas/diagrama_$EXERCICIO.png $DEST/
cp -r src/main/java/padroescriacao/$EXERCICIO $DEST/src/main/java/padroescriacao/
cp -r src/test/java/padroescriacao/$EXERCICIO $DEST/src/test/java/padroescriacao/

# repositório
cd $DEST

# create a readme file
echo "# Padrões de projeto
 ____
  ## $EXERCICIO<br><br>![diagrama_$EXERCICIO.png](diagrama_$EXERCICIO.png)" > README.md

git init
git add .
git commit -m "Isto eh um commit automatico $EXERCICIO"
git branch -M main


# todo: create repository with name $EXERCICIO on github

echo "Digite a URL do repositório remoto: "
read url

# aadicionar repositório remoto
git remote add origin $url
git push --force origin main

echo "Repositório criado com sucesso"
echo "Repositório remoto adicionado com sucesso em $url"
