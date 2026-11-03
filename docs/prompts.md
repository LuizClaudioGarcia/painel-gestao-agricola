# 🤖 Uso de IA

Usei IA como par de programação: ela escrevia o código, e eu definia a arquitetura, revisava e validava cada entrega. Não está aqui 100% do que foi conversado, mas dá uma boa noção de como eu trabalhei com a IA.

## Como trabalhei

- **Regras antes do código.** Escrevi antes um arquivo com a stack, o padrão do projeto e as decisões que não podiam mudar.
- **Por etapas.** Banco e entidades, cálculo, consulta analítica, controllers, front e README. No começo cada parte virou um commit pequeno. Perto do prazo, passei a juntar mais coisa por commit.
- **Revisão e Commit.** Nada subiu sem ter sido revisado, conferido, rodado e validado.

## Exemplos de pedidos (reescritos, não copiados do chat)

**Cálculo financeiro**

> Agora vamos pro cálculo financeiro. A ideia é que dê pra colocar uma entidade nova no futuro sem mexer no que já existe, então nada de `instanceof` ou if checando tipo. Cria uma interface `CalculavelFinanceiro` e faz o `Produtor`, a `Propriedade` e o `Laboratorio` implementarem. Faz também um teste unitário sem Spring e sem banco, passando um lambda qualquer, pra provar que funciona com algo que nem é entidade.

**Consulta analítica**

> Essa consulta analítica vai em SQL nativo no PostgreSQL, via JDBC. Agrega no banco: `GROUP BY` do laboratório, `COUNT` dos produtores e `HAVING` na quantidade mínima. Não faz `JOIN` em `produtor_propriedade`: o N:N infla o result set e o `COUNT` duplica produtor. Filtros por intervalo de `timestamptz` e `ILIKE` nas observações, tudo com parâmetro nomeado. A cláusula `ORDER BY` não vem da URL — duas constantes no código. Teste de integração com Testcontainers no PostgreSQL; nada de H2, que não tem `ILIKE` nem trata `timestamptz` igual.

**Front-end**

> O link do Figma está anexado. Monta com Material UI, usando o `Table` com o `TablePagination` na listagem. Toda tela precisa tratar carregando, erro com tentar de novo e lista vazia. O ID que aparece pro usuário é o código, não o UUID. São três telas: [...]. Nos campos de inscrição, valida CPF e CNPJ. Se o usuário tentar enviar sem preencher o obrigatório, marca o campo com borda vermelha e mostra um toaster de erro [...]

Eu teria preferido gerar a base das telas pelo Figma Make, mas não tinha acesso no momento.

## Onde eu corrigi a IA

- **UUID.** Ela usou v4 (`randomUUID()`), que espalha os inserts no índice. Troquei por v7 com a `uuid-creator`, porque o `Style.TIME` do Hibernate 6.6 gera v1, não v7.
- **Ordem das etapas.** Coloquei o CRUD do produtor antes do cálculo, pra testar cedo pelo Swagger.
- **Mensagens de validação.** Passei a usar o nome do campo como está na API, por exemplo "quantidadeMinima começa em 1.".
- **Premissas.** Decidi o vínculo N para N entre produtor e propriedade, mantive a inscrição fiscal única e confirmei com o RH que as fórmulas ficavam a meu critério.

## O que foi decisão minha

PostgreSQL com herança `JOINED`, Flyway e UUID v7. MVC simples, com DTO, sem hexagonal nem MapStruct. Cálculo feito usando polimorfismo. Ordenação só por constantes do código, parâmetros nomeados e um handler de erros central. Testes sem H2 e sem Mockito: regra com objetos reais e consulta contra um Postgres de verdade.
