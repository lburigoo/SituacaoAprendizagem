// URL da API - constante pra facilitar manutencao
const API_URL = '/api/colaboradores';

// funcao pra mostrar mensagens na tela
function mostrarMensagem(texto, tipo) {
    var div = document.getElementById('mensagem');
    div.innerHTML = '<div class="message message-' + tipo + '">' + texto + '</div>';
    // some depois de 5 segundos
    setTimeout(function() {
        div.innerHTML = '';
    }, 5000);
}

// funcao pra formatar CPF (coloca os pontos e tracos)
function formatarCpf(cpf) {
    // remove tudo que nao é numero
    cpf = cpf.replace(/\D/g, '');
    if (cpf.length === 11) {
        return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
    }
    return cpf;
}

// funcao pra formatar telefone
function formatarTelefone(tel) {
    tel = tel.replace(/\D/g, '');
    if (tel.length === 11) {
        return tel.replace(/(\d{2})(\d{5})(\d{4})/, '($1) $2-$3');
    }
    if (tel.length === 10) {
        return tel.replace(/(\d{2})(\d{4})(\d{4})/, '($1) $2-$3');
    }
    return tel;
}

// carrega a lista de colaboradores da API
async function carregarColaboradores() {
    try {
        var response = await fetch(API_URL);
        var data = await response.json();

        var tbody = document.getElementById('tabelaColaboradores');

        // se nao tem nenhum, mostra mensagem
        if (data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;color:#999;">Nenhum colaborador cadastrado</td></tr>';
            return;
        }

        // monta as linhas da tabela
        var html = '';
        for (var i = 0; i < data.length; i++) {
            var col = data[i];
            html += '<tr>';
            html += '<td>' + col.id + '</td>';
            html += '<td>' + col.nome + '</td>';
            html += '<td>' + formatarCpf(col.cpf) + '</td>';
            html += '<td>' + (col.cargo || '-') + '</td>';
            html += '<td>' + (col.setor || '-') + '</td>';
            html += '<td>' + (col.telefone ? formatarTelefone(col.telefone) : '-') + '</td>';
            html += '<td><span class="badge ' + (col.ativo ? 'badge-success' : 'badge-danger') + '">' + (col.ativo ? 'Ativo' : 'Inativo') + '</span></td>';
            html += '<td>';
            html += '<button class="btn btn-warning btn-sm" onclick="editar(' + col.id + ')">Editar</button> ';
            html += '<button class="btn btn-danger btn-sm" onclick="excluir(' + col.id + ')">Excluir</button>';
            html += '</td>';
            html += '</tr>';
        }
        tbody.innerHTML = html;

    } catch (error) {
        document.getElementById('tabelaColaboradores').innerHTML =
            '<tr><td colspan="8" style="text-align:center;color:#c62828;">Erro ao carregar dados</td></tr>';
    }
}

// funcao de buscar por nome
async function buscarColaboradores() {
    var nome = document.getElementById('campoBusca').value.trim();

    // se o campo estiver vazio, carrega tudo
    if (!nome) {
        carregarColaboradores();
        return;
    }

    try {
        var response = await fetch(API_URL + '/buscar?nome=' + encodeURIComponent(nome));
        var data = await response.json();

        var tbody = document.getElementById('tabelaColaboradores');

        if (data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;color:#999;">Nenhum colaborador encontrado</td></tr>';
            return;
        }

        var html = '';
        for (var i = 0; i < data.length; i++) {
            var col = data[i];
            html += '<tr>';
            html += '<td>' + col.id + '</td>';
            html += '<td>' + col.nome + '</td>';
            html += '<td>' + formatarCpf(col.cpf) + '</td>';
            html += '<td>' + (col.cargo || '-') + '</td>';
            html += '<td>' + (col.setor || '-') + '</td>';
            html += '<td>' + (col.telefone ? formatarTelefone(col.telefone) : '-') + '</td>';
            html += '<td><span class="badge ' + (col.ativo ? 'badge-success' : 'badge-danger') + '">' + (col.ativo ? 'Ativo' : 'Inativo') + '</span></td>';
            html += '<td>';
            html += '<button class="btn btn-warning btn-sm" onclick="editar(' + col.id + ')">Editar</button> ';
            html += '<button class="btn btn-danger btn-sm" onclick="excluir(' + col.id + ')">Excluir</button>';
            html += '</td>';
            html += '</tr>';
        }
        tbody.innerHTML = html;

    } catch (error) {
        mostrarMensagem('Erro ao buscar colaboradores', 'error');
    }
}

// abre o modal pra cadastrar novo
function abrirModal() {
    document.getElementById('modalTitulo').textContent = 'Novo Colaborador';
    document.getElementById('formColaborador').reset();
    document.getElementById('colaboradorId').value = '';
    document.getElementById('ativo').checked = true;
    document.getElementById('modal').classList.add('active');
}

// fecha o modal
function fecharModal() {
    document.getElementById('modal').classList.remove('active');
}

// abrir modal pra editar um colaborador existente
async function editar(id) {
    try {
        var response = await fetch(API_URL + '/' + id);
        var col = await response.json();

        document.getElementById('modalTitulo').textContent = 'Editar Colaborador';
        document.getElementById('colaboradorId').value = col.id;
        document.getElementById('nome').value = col.nome;
        document.getElementById('cpf').value = col.cpf;
        document.getElementById('cargo').value = col.cargo || '';
        document.getElementById('setor').value = col.setor || '';
        document.getElementById('telefone').value = col.telefone || '';
        document.getElementById('ativo').checked = col.ativo;

        document.getElementById('modal').classList.add('active');
    } catch (error) {
        mostrarMensagem('Erro ao carregar dados do colaborador', 'error');
    }
}

// exclui (desativa) um colaborador
async function excluir(id) {
    if (!confirm('Tem certeza que deseja desativar este colaborador?')) {
        return;
    }

    try {
        var response = await fetch(API_URL + '/' + id, { method: 'DELETE' });
        var result = await response.json();

        if (response.ok) {
            mostrarMensagem(result.mensagem || 'Colaborador desativado com sucesso', 'success');
            carregarColaboradores();
        } else {
            mostrarMensagem(result.erro || 'Erro ao desativar colaborador', 'error');
        }
    } catch (error) {
        mostrarMensagem('Erro ao conectar com o servidor', 'error');
    }
}

// evento do formulario - salvar ou atualizar
document.getElementById('formColaborador').addEventListener('submit', async function(e) {
    e.preventDefault();

    var id = document.getElementById('colaboradorId').value;

    var dados = {
        nome: document.getElementById('nome').value,
        cpf: document.getElementById('cpf').value,
        cargo: document.getElementById('cargo').value,
        setor: document.getElementById('setor').value,
        telefone: document.getElementById('telefone').value,
        ativo: document.getElementById('ativo').checked
    };

    try {
        var url = id ? API_URL + '/' + id : API_URL;
        var method = id ? 'PUT' : 'POST';

        var response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });

        var result = await response.json();

        if (response.ok) {
            var msg = id ? 'Colaborador atualizado com sucesso' : 'Colaborador cadastrado com sucesso';
            mostrarMensagem(msg, 'success');
            fecharModal();
            carregarColaboradores();
        } else {
            mostrarMensagem(result.erro || 'Erro ao salvar colaborador', 'error');
        }
    } catch (error) {
        mostrarMensagem('Erro ao conectar com o servidor', 'error');
    }
});

// carrega a lista quando a pagina abre
carregarColaboradores();
