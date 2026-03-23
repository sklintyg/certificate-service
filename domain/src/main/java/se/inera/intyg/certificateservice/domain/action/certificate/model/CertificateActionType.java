/*
 * Copyright (C) 2026 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.certificateservice.domain.action.certificate.model;

public enum CertificateActionType {
  CREATE,
  READ,
  UPDATE,
  DELETE,
  SIGN,
  SEND,
  PRINT,
  REVOKE,
  REPLACE,
  REPLACE_CONTINUE,
  RENEW,
  SEND_AFTER_SIGN,
  SEND_AFTER_COMPLEMENT,
  COMPLEMENT,
  CANNOT_COMPLEMENT,
  MESSAGES,
  MESSAGES_ADMINISTRATIVE,
  FORWARD_MESSAGE,
  HANDLE_COMPLEMENT,
  RECEIVE_COMPLEMENT,
  RECEIVE_QUESTION,
  RECEIVE_ANSWER,
  RECEIVE_REMINDER,
  CREATE_MESSAGE,
  ANSWER_MESSAGE,
  SAVE_MESSAGE,
  DELETE_MESSAGE,
  SEND_MESSAGE,
  SAVE_ANSWER,
  DELETE_ANSWER,
  SEND_ANSWER,
  HANDLE_MESSAGE,
  FORWARD_CERTIFICATE,
  READY_FOR_SIGN,
  LIST_CERTIFICATE_TYPE,
  QUESTIONS_NOT_AVAILABLE,
  FORWARD_CERTIFICATE_FROM_LIST,
  FMB,
  SRS_DRAFT,
  SRS_SIGNED,
  CREATE_DRAFT_FROM_CERTIFICATE,
  UPDATE_DRAFT_FROM_CERTIFICATE,
  INACTIVE_CERTIFICATE_MODEL
}
