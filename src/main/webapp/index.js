var jobId = '';

$('#div_JobStatus').hide();

$('#a_SubmitJob').click(
		function() {
			$('#div_JobStatus').show();
			$('#div_SubmitJobForm').hide();

			var userEmail = $('#input_userEmail').val();
			var documentUrl = $('#input_documentUrl').val();
			$.post('jobs/', {
				userEmail : userEmail,
				documentUrl : documentUrl
			}, function(job) {
				jobId = job.id;
				$('#div_jobStatus').html(job.status);
				setInterval(function() {
					$.get('jobs/' + job.id, function(job) {
						var jobProgress =
								(job.progress * 100.0 / job.totalWork);
						$('#div_jobStatus').html(job.status);
						$('#div_jobId').html(job.id);
						$('#div_progressBar').html(jobProgress + '%');
						$('#div_progressBar').attr(
								'style',
								'width: ' + jobProgress + '%;');
					});
				}, 3000);

			});

		});

function sendJobAction(action) {
	$.post('jobs/' + jobId + '/action', {
		action : action
	});
}

$('#a_cancelJob').click(function() {
	sendJobAction('CANCEL');
});
$('#a_pauseJob').click(function() {
	sendJobAction('PAUSE');
});
$('#a_resumeJob').click(function() {
	sendJobAction('RESUME');
});
$('#a_restartJob').click(function() {
	sendJobAction('RESTART');
});