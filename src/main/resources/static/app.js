const API_URL = "/agendamentos";

const listaAgendamentos = document.getElementById("listaAgendamentos");
const contadorAgendamentos = document.getElementById("contadorAgendamentos");
const modalAgendamento = document.getElementById("modalAgendamento");
const formAgendamento = document.getElementById("formAgendamento");
const btnNovoAgendamento = document.getElementById("btnNovoAgendamento");
const btnCloseModal = document.getElementById("btnCloseModal");
const btnCancelarForm = document.getElementById("btnCancelarForm");

const telaApresentacao = document.getElementById("telaApresentacao");
const telaLogin = document.getElementById("telaLogin");
const ambienteSistema = document.getElementById("ambienteSistema");
const formLogin = document.getElementById("formLogin");
const inputUsuarioLogin = document.getElementById("inputUsuarioLogin");
const inputSenhaLogin = document.getElementById("inputSenhaLogin");
const nomeUsuarioSessao = document.getElementById("nomeUsuarioSessao");
const inputUsuarioForm = document.getElementById("usuario");

const ajusteExpediente = document.getElementById("ajusteExpediente");
const ajusteRetroativo = document.getElementById("ajusteRetroativo");

let usuarioLogado = "";
let senhaLogada = "";
let cacheAgendamentos = [];
let chartInstance = null;

function irParaLogin() {
    telaApresentacao.classList.add("hidden");
    telaLogin.classList.remove("hidden");
}

function voltarParaApresentacao() {
    telaLogin.classList.add("hidden");
    telaApresentacao.classList.remove("hidden");
}

formLogin.addEventListener("submit", (e) => {
    e.preventDefault();
    usuarioLogado = inputUsuarioLogin.value.trim().toLowerCase();
    senhaLogada = inputSenhaLogin.value;

    if (usuarioLogado && senhaLogada) {
        nomeUsuarioSessao.innerText = usuarioLogado;
        inputUsuarioForm.value = usuarioLogado;
        telaLogin.classList.add("hidden");
        ambienteSistema.classList.remove("hidden");
        carregarAgendamentos();
    }
});

function logout() {
    usuarioLogado = "";
    senhaLogada = "";
    inputUsuarioLogin.value = "";
    inputSenhaLogin.value = "";
    navegar('aba-agendamentos');
    ambienteSistema.classList.add("hidden");
    telaApresentacao.classList.remove("hidden");
}

function vincularEventos() {
    btnNovoAgendamento.addEventListener("click", abrirModal);
    btnCloseModal.addEventListener("click", fecharModal);
    btnCancelarForm.addEventListener("click", fecharModal);
    formAgendamento.addEventListener("submit", criarAgendamento);
}
vincularEventos();

function navegar(idAba) {
    document.querySelectorAll('.content-view').forEach(aba => aba.classList.add('hidden'));
    document.querySelectorAll('.nav-btn').forEach(btn => btn.classList.remove('active'));

    document.getElementById(idAba).classList.remove('hidden');
    document.getElementById(`nav-${idAba}`).classList.add('active');

    if (idAba === 'aba-metricas') {
        processarMetricas();
    }
}

function abrirModal() {
    formAgendamento.reset();
    inputUsuarioForm.value = usuarioLogado;
    modalAgendamento.classList.add("active");
}

function fecharModal() {
    modalAgendamento.classList.remove("active");
}

async function carregarAgendamentos() {
    listaAgendamentos.innerHTML = `
        <div class="col-span-full text-center py-12 text-slate-400 font-medium">
            <i class="fa-solid fa-circle-notch fa-spin text-2xl text-indigo-600 block mb-3"></i>
            Isolando chaves e consultando registros de ${usuarioLogado}...
        </div>`;

    try {
        const response = await fetch(`${API_URL}?usuario=${encodeURIComponent(usuarioLogado)}`);
        if (!response.ok) throw new Error("A requisição falhou no servidor.");

        cacheAgendamentos = await response.json();
        renderizarCards(cacheAgendamentos);
    } catch (error) {
        listaAgendamentos.innerHTML = `
            <div class="col-span-full text-center py-12 text-rose-500 font-semibold">
                <i class="fa-solid fa-triangle-exclamation text-3xl block mb-3"></i>
                Falha na conexão: ${error.message}
            </div>`;
    }
}

function renderizarCards(lista) {
    if (lista.length === 0) {
        contadorAgendamentos.innerText = "0 ativos";
        listaAgendamentos.innerHTML = `
            <div class="col-span-full text-center py-12 text-slate-400 bg-white border border-slate-200 border-dashed rounded-2xl">
                <i class="fa-regular fa-folder-open text-4xl block mb-3 text-slate-300"></i>
                Nenhum compromisso agendado no ecossistema para ${usuarioLogado}.
            </div>`;
        return;
    }

    const ativos = lista.filter(a => a.status === "AGENDADO").length;
    contadorAgendamentos.innerText = `${ativos} ativo${ativos !== 1 ? 's' : ''}`;

    listaAgendamentos.innerHTML = lista.map(ag => {
        const dataInicio = new Date(ag.dataInicio).toLocaleString('pt-BR', { dateStyle: 'short', timeStyle: 'short' });
        const dataFim = new Date(ag.dataFim).toLocaleString('pt-BR', { timeStyle: 'short' });

        return `
            <div class="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm relative flex flex-col justify-between hover:shadow-md transition-all duration-200">
                <span class="absolute top-6 right-6 text-xs font-bold px-2.5 py-1 rounded-full uppercase tracking-wider status-${ag.status}">
                    ${ag.status}
                </span>
                <div class="mb-5">
                    <span class="text-xs font-bold text-indigo-600 bg-indigo-50 px-2 py-1 rounded-md inline-flex items-center gap-1.5 mb-3">
                        <i class="fa-solid fa-circle-user text-[10px]"></i> ${ag.usuario}
                    </span>
                    <h4 class="text-lg font-bold text-slate-900 truncate pr-20" title="${ag.titulo}">${ag.titulo}</h4>
                    <p class="text-slate-500 text-sm leading-relaxed mt-2 line-clamp-2">${ag.descricao}</p>
                </div>
                <div>
                    <div class="bg-slate-50 text-slate-700 text-xs font-semibold p-3 rounded-xl flex items-center gap-2 mb-4 border border-slate-100">
                        <i class="fa-regular fa-calendar text-slate-400 text-sm"></i> ${dataInicio} até ${dataFim}
                    </div>
                    <div class="flex gap-2 border-t border-slate-100 pt-4">
                        ${ag.status === 'AGENDADO' ? `
                            <button class="text-emerald-600 hover:bg-emerald-50 text-xs font-bold py-2 px-3 rounded-lg flex items-center gap-1 transition-all" onclick="concluir(${ag.id})">
                                <i class="fa-solid fa-check"></i> Concluir
                            </button>
                            <button class="text-rose-600 hover:bg-rose-50 text-xs font-bold py-2 px-3 rounded-lg flex items-center gap-1 transition-all" onclick="cancelar(${ag.id})">
                                <i class="fa-solid fa-ban"></i> Cancelar
                            </button>
                        ` : `<span class="text-xs font-medium text-slate-400 italic">Fluxo Encerrado</span>`}
                    </div>
                </div>
            </div>`;
    }).join("");
}

async function criarAgendamento(e) {
    e.preventDefault();

    const dtInicio = new Date(document.getElementById("dataInicio").value);
    const dtFim = new Date(document.getElementById("dataFim").value);
    const agora = new Date();

    if (ajusteRetroativo.checked && dtInicio < agora) {
        alert("⚠️ Regra de Ajuste Ativa: Não é permitido criar agendamentos em datas retroativas.");
        return;
    }

    if (ajusteExpediente.checked) {
        const horaInicio = dtInicio.getHours();
        const horaFim = dtFim.getHours();
        if (horaInicio < 8 || horaInicio > 22 || horaFim < 8 || horaFim > 22) {
            alert("⚠️ Regra de Ajuste Ativa: Fora do Horário Comercial permitido (08h às 22h).");
            return;
        }
    }

    const payload = {
        titulo: document.getElementById("titulo").value,
        usuario: usuarioLogado,
        senha: senhaLogada,
        dataInicio: document.getElementById("dataInicio").value,
        dataFim: document.getElementById("dataFim").value,
        descricao: document.getElementById("descricao").value
    };

    try {
        const response = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        if (!response.ok) throw new Error("Conflito de horários ou erro estrutural.");

        fecharModal();
        carregarAgendamentos();
    } catch (error) {
        alert("⚠️ Erro no Java: " + error.message);
    }
}

async function concluir(id) {
    try {
        const response = await fetch(`${API_URL}/${id}/concluir`, { method: "PUT" });
        if (response.ok) carregarAgendamentos();
    } catch (error) {
        alert("Erro ao concluir.");
    }
}

async function cancelar(id) {
    if (!confirm("Confirmar cancelamento definitivo?")) return;
    try {
        const response = await fetch(`${API_URL}/${id}/cancelar`, { method: "PUT" });
        if (response.ok) carregarAgendamentos();
    } catch (error) {
        alert("Erro ao cancelar.");
    }
}

function processarMetricas() {
    const total = cacheAgendamentos.length;
    const concluidos = cacheAgendamentos.filter(a => a.status === "CONCLUIDO").length;
    const cancelados = cacheAgendamentos.filter(a => a.status === "CANCELADO").length;
    const agendados = cacheAgendamentos.filter(a => a.status === "AGENDADO").length;

    document.getElementById("metricTotal").innerText = total;
    document.getElementById("metricConcluidos").innerText = total > 0 ? `${Math.round((concluidos/total)*100)}%` : "0%";
    document.getElementById("metricCancelados").innerText = total > 0 ? `${Math.round((cancelados/total)*100)}%` : "0%";

    if (chartInstance) {
        chartInstance.destroy();
    }

    const ctx = document.getElementById('chartStatus').getContext('2d');
    chartInstance = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: ['Ativos', 'Concluídos', 'Cancelados'],
            datasets: [{
                data: [agendados, concluidos, cancelados],
                backgroundColor: ['#4f46e5', '#10b981', '#ef4444'],
                borderWidth: 0
            }]
        },
        options: {
            plugins: { legend: { position: 'bottom', labels: { boxWidth: 12, font: { family: 'Inter', weight: 500 } } } }
        }
    });
}