// URL da API de EPIs
const API_URL = '/api/epis';

// mostra mensagem na tela
function mostrarMensagem(texto, tipo) {
    var div = document.getElementById('mensagem');
    div.innerHTML = '<div class="message message-' + tipo + '">' + texto + '</div>';
    setTimeout(function() {
        div.innerHTML = '';
    }, 5000);
}

// formata data no padrao brasileiro
function formatarData(data) {
    if (!data) return '-';
    var d = new Date(data + 'T00:00:00');
    return d.toLocaleDateString('pt-BR');
}

// carrega todos os EPIs
async function carregarEpis() {
    try {
        var response = await fetch(API_URL);
        var data = await response.json();

        var tbody = document.getElementById('tabelaEpis');

        if (data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" style="text-align:center;color:#999;">Nenhum EPI cadastrado</td></tr>';
            return;
        }

        var html = '';
        for (var i = 0; i < data.length; i++) {
            var epi = data[i];
            html += '<tr>';
            html += '<td>' + epi.id + '</td>';
            html += '<td>' + epi.nome + '</td>';
            html += '<td>' + (epi.categoria || '-') + '</td>';
            html += '<td>' + (epi.certificadoCA || '-') + '</td>';
            html += '<td>' + (epi.fabricante || '-') + '</td>';
            html += '<td><strong>' + epi.quantidadeEstoque + '</strong></td>';
            html += '<td>' + formatarData(epi.dataValidade) + '</td>';
            html += '<td><span class="badge ' + (epi.ativo ? 'badge-success' : 'badge-danger') + '">' + (epi.ativo ? 'Ativo' : 'Inativo') + '</span></td>';
            html += '<td>';
            html += '<button class="btn btn-warning btn-sm" onclick="editar(' + epi.id + ')">Editar</button> ';
            html += '<button class="btn btn-danger btn-sm" onclick="excluir(' + epi.id + ')">Excluir</button>';
            html += '</td>';
            html += '</tr>';
        }
        tbody.innerHTML = html;

    } catch (error) {
        document.getElementById('tabelaEpis').innerHTML =
            '<tr><td colspan="8" style="text-align:center;color:#c62828;">Erro ao carregar dados</td></tr>';
    }
}

// busca EPIs por nome
async function buscarEpis() {
    var nome = document.getElementById('campoBusca').value.trim();

    if (!nome) {
        carregarEpis();
        return;
    }

    try {
        var response = await fetch(API_URL + '/buscar?nome=' + encodeURIComponent(nome));
        var data = await response.json();

        var tbody = document.getElementById('tabelaEpis');

        if (data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="9" style="text-align:center;color:#999;">Nenhum EPI encontrado</td></tr>';
            return;
        }

        var html = '';
        for (var i = 0; i < data.length; i++) {
            var epi = data[i];
            html += '<tr>';
            html += '<td>' + epi.id + '</td>';
            html += '<td>' + epi.nome + '</td>';
            html += '<td>' + (epi.categoria || '-') + '</td>';
            html += '<td>' + (epi.certificadoCA || '-') + '</td>';
            html += '<td>' + (epi.fabricante || '-') + '</td>';
            html += '<td><strong>' + epi.quantidadeEstoque + '</strong></td>';
            html += '<td>' + formatarData(epi.dataValidade) + '</td>';
            html += '<td><span class="badge ' + (epi.ativo ? 'badge-success' : 'badge-danger') + '">' + (epi.ativo ? 'Ativo' : 'Inativo') + '</span></td>';
            html += '<td>';
            html += '<button class="btn btn-warning btn-sm" onclick="editar(' + epi.id + ')">Editar</button> ';
            html += '<button class="btn btn-danger btn-sm" onclick="excluir(' + epi.id + ')">Excluir</button>';
            html += '</td>';
            html += '</tr>';
        }
        tbody.innerHTML = html;

    } catch (error) {
        mostrarMensagem('Erro ao buscar EPIs', 'error');
    }
}

function abrirModal() {
    document.getElementById('modalTitulo').textContent = 'Novo EPI';
    document.getElementById('formEpi').reset();
    document.getElementById('epiId').value = '';
    document.getElementById('ativo').checked = true;
    document.getElementById('quantidadeEstoque').value = 0;
    document.getElementById('modal').classList.add('active');
}

function fecharModal() {
    document.getElementById('modal').classList.remove('active');
}

async function editar(id) {
    try {
        var response = await fetch(API_URL + '/' + id);
        var epi = await response.json();

        document.getElementById('modalTitulo').textContent = 'Editar EPI';
        document.getElementById('epiId').value = epi.id;
        document.getElementById('nome').value = epi.nome;
        document.getElementById('descricao').value = epi.descricao || '';
        document.getElementById('certificadoCA').value = epi.certificadoCA || '';
        document.getElementById('categoria').value = epi.categoria || '';
        document.getElementById('fabricante').value = epi.fabricante || '';
        document.getElementById('quantidadeEstoque').value = epi.quantidadeEstoque;
        document.getElementById('dataValidade').value = epi.dataValidade || '';
        document.getElementById('ativo').checked = epi.ativo;

        document.getElementById('modal').classList.add('active');
    } catch (error) {
        mostrarMensagem('Erro ao carregar dados do EPI', 'error');
    }
}

async function excluir(id) {
    if (!confirm('Tem certeza que deseja desativar este EPI?')) {
        return;
    }

    try {
        var response = await fetch(API_URL + '/' + id, { method: 'DELETE' });
        var result = await response.json();

        if (response.ok) {
            mostrarMensagem(result.mensagem || 'EPI desativado com sucesso', 'success');
            carregarEpis();
        } else {
            mostrarMensagem(result.erro || 'Erro ao desativar EPI', 'error');
        }
    } catch (error) {
        mostrarMensagem('Erro ao conectar com o servidor', 'error');
    }
}

// evento do formulario
document.getElementById('formEpi').addEventListener('submit', async function(e) {
    e.preventDefault();

    var id = document.getElementById('epiId').value;

    var dados = {
        nome: document.getElementById('nome').value,
        descricao: document.getElementById('descricao').value,
        certificadoCA: document.getElementById('certificadoCA').value,
        categoria: document.getElementById('categoria').value || null,
        fabricante: document.getElementById('fabricante').value,
        quantidadeEstoque: parseInt(document.getElementById('quantidadeEstoque').value) || 0,
        dataValidade: document.getElementById('dataValidade').value || null,
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
            var msg = id ? 'EPI atualizado com sucesso' : 'EPI cadastrado com sucesso';
            mostrarMensagem(msg, 'success');
            fecharModal();
            carregarEpis();
        } else {
            mostrarMensagem(result.erro || 'Erro ao salvar EPI', 'error');
        }
    } catch (error) {
        mostrarMensagem('Erro ao conectar com o servidor', 'error');
    }
});

// inicia a pagina carregando os dados
carregarEpis();
