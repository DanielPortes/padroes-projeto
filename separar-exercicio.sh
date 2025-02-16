#!/bin/bash

# Interrompe em caso de erro
set -e

# Solicita o nome completo do exercício ou pasta (ex: comportamental/exercicio04)
read -p "Digite o nome do exercício (ex: comportamental/exercicio04): " EXERCICIO

# -------------------------
# PARTE 1: CONFIGURAÇÕES E VARIÁVEIS
# -------------------------

# Nome do repositório local e no GitHub (troca barra por hífen para evitar problemas)
REPO_NAME=$(echo "$EXERCICIO" | sed 's|/|-|g')

# Caminho do repositório local temporário
DEST="../${REPO_NAME}-repo"

# Usuário ou organização do GitHub (altere para o seu)
GITHUB_USER="SEU_USUARIO_GITHUB_AQUI"

# URL do repositório remoto no GitHub
REPO_URL="https://github.com/$GITHUB_USER/$REPO_NAME.git"

echo ""
echo "======================================"
echo " Exercício: $EXERCICIO"
echo " Repositório local: $DEST"
echo " Repositório GitHub: $REPO_URL"
echo "======================================"
echo ""

# Verifica se o repositório já existe no GitHub usando CLI oficial
echo "Verificando se o repositório já existe no GitHub..."
if gh repo view "$GITHUB_USER/$REPO_NAME" &> /dev/null
then
  echo "Repositório $REPO_NAME já existe no GitHub. Não será recriado."
  REPO_EXISTS=true
else
  echo "Repositório $REPO_NAME não existe. Será criado no GitHub."
  REPO_EXISTS=false
fi

# -------------------------
# PARTE 2: CRIA ESTRUTURA LOCAL
# -------------------------

# Remove pasta de destino se já existir (opcional, para garantir ambiente limpo)
rm -rf "$DEST"

echo "Criando estrutura de diretórios em $DEST..."
mkdir -p "$DEST/src/main/java/$EXERCICIO"
mkdir -p "$DEST/src/test/java/$EXERCICIO"
mkdir -p "$DEST/diagramas"

# -------------------------
# PARTE 3: COPIA ARQUIVOS DO PROJETO
# -------------------------

echo "Copiando arquivos necessários (.gitignore, pom.xml, README.md etc.)..."

[ -f .gitignore ] && cp .gitignore "$DEST/" || echo "Aviso: .gitignore não encontrado."
[ -f pom.xml ] && cp pom.xml "$DEST/" || echo "Aviso: pom.xml não encontrado."
[ -f README.md ] && cp README.md "$DEST/" || echo "Aviso: README.md não encontrado."

# Se houver um diagrama pré-existente com o nome do exercício
[ -f "./diagramas/diagrama_${REPO_NAME}.png" ] && cp "./diagramas/diagrama_${REPO_NAME}.png" "$DEST/diagramas" || echo "Aviso: Diagrama não encontrado."

# Copia o código-fonte do exercício, se existir
if [ -d "src/main/java/$EXERCICIO" ]; then
    cp -r "src/main/java/$EXERCICIO" "$DEST/src/main/java/$EXERCICIO"
else
    echo "Aviso: Código fonte não encontrado em src/main/java/$EXERCICIO."
fi

# Copia os testes do exercício, se existirem
if [ -d "src/test/java/$EXERCICIO" ]; then
    cp -r "src/test/java/$EXERCICIO" "$DEST/src/test/java/$EXERCICIO"
else
    echo "Aviso: Testes não encontrados em src/test/java/$EXERCICIO."
fi

# -------------------------
# PARTE 4: (OPCIONAL) GERAÇÃO AUTOMÁTICA DE DIAGRAMA UML
# -------------------------
# Exemplo usando PlantUML em linha de comando, caso queira usar outra ferramenta, adapte.
echo "Gerando diagrama UML automaticamente (se PlantUML estiver instalado e configurado)..."
if command -v plantuml &> /dev/null
then
  # Gerando um diagrama simples a partir de um .puml de exemplo (adapte se necessário)
  # Supondo que exista um "diagrama.puml" ou algo do tipo. Ajuste para seu caso real.
  if [ -f "./diagramas/diagrama_${REPO_NAME}.puml" ]; then
    plantuml -o "$DEST/diagramas" "./diagramas/diagrama_${REPO_NAME}.puml"
    echo "Diagrama UML gerado e copiado para $DEST/diagramas."
  else
    echo "Nenhum arquivo .puml encontrado para gerar diagrama automático."
  fi
else
  echo "PlantUML não instalado ou não encontrado no PATH. Pulei geração automática."
fi

# -------------------------
# PARTE 5: INICIALIZA GIT LOCAL E CONFIGURA README
# -------------------------
echo "Inicializando repositório Git local..."
cd "$DEST" || exit
git init

# Sobrescreve/cria README.md
cat <<EOF > README.md
# Padrões de Projeto

## $EXERCICIO

### Estrutura de pastas
\`\`\`
src
 ├─ main
 │   └─ java
 │       └─ $EXERCICIO
 └─ test
     └─ java
         └─ $EXERCICIO
\`\`\`

### Diagramas
Se houver diagrama, ele estará na pasta \`diagramas\`. Exemplo:
\`\`\`
diagramas/
 └─ diagrama_${REPO_NAME}.png (se existir)
\`\`\`
EOF

git add .

# Cria um commit inicial vazio apenas para "marcar" a data (opcional)
echo "Criando commit inicial (vazio)..."
git commit --allow-empty -m "Iniciando exercício $EXERCICIO"

# -------------------------
# PARTE 6: PERGUNTA SE DESEJA MODIFICAR DATA DO COMMIT FINAL
# -------------------------
read -p "Deseja modificar a data do commit final para algum dia específico? (s/n) " ALTERAR_DATA
if [ "$ALTERAR_DATA" == "s" ]; then
  read -p "Digite a data do commit (YYYY-MM-DD): " DATA_COMMIT
  # Gera horário aleatório (hora, minuto, segundo)
  HORA=$(shuf -i 0-23 -n 1)
  MINUTO=$(shuf -i 0-59 -n 1)
  SEGUNDO=$(shuf -i 0-59 -n 1)
  COMMIT_DATE="${DATA_COMMIT}T$(printf "%02d:%02d:%02d" $HORA $MINUTO $SEGUNDO)"

  echo "Data do commit será: $COMMIT_DATE"
  GIT_AUTHOR_DATE="$COMMIT_DATE" GIT_COMMITTER_DATE="$COMMIT_DATE" git commit -am "Adicionando arquivos para $EXERCICIO"
else
  git commit -am "Adicionando arquivos para $EXERCICIO"
fi

# -------------------------
# PARTE 7: CRIA REPOSITÓRIO NO GITHUB (SE NÃO EXISTIR) E FAZ PUSH
# -------------------------
if [ "$REPO_EXISTS" = false ]; then
  echo "Criando repositório no GitHub via CLI..."
  # --public, --private ou --visibility=internal (dependendo do que quiser)
  gh repo create "$GITHUB_USER/$REPO_NAME" --public --source=. --remote=origin --push
else
  echo "Adicionando remote e fazendo push para repositório já existente..."
  git remote add origin "$REPO_URL" || true
  git branch -M main
  git push -u origin main
fi

# -------------------------
# PARTE 8: ABRE O REPOSITÓRIO NO NAVEGADOR
# -------------------------
echo "Abrindo repositório no navegador..."
if command -v xdg-open &> /dev/null
then
  xdg-open "https://github.com/$GITHUB_USER/$REPO_NAME"
elif command -v open &> /dev/null
then
  open "https://github.com/$GITHUB_USER/$REPO_NAME"
else
  echo "Não foi possível abrir o navegador automaticamente. Acesse:"
  echo "https://github.com/$GITHUB_USER/$REPO_NAME"
fi

# -------------------------
# PARTE 9: LIMPEZA FINAL (OPCIONAL)
# -------------------------
# Caso você queira remover a pasta local depois de enviar para o GitHub, descomente:
# cd ..
# rm -rf "$DEST"

echo ""
echo "======================================"
echo "Repositório criado/enviado com sucesso!"
echo "======================================"
