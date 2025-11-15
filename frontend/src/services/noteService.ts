import { apiCall } from "./api";

export const noteService = {
  async createNote(data: {
    title: string;
    content?: string;
    noteType: 'TEXT' | 'IMAGE' | 'AUDIO' | 'DRAWING' | 'TEMPLATE';
    visibility?: 'PUBLIC' | 'PRIVATE' | 'GROUP';
    groupId?: number;
    tagIds?: number[];
  }) {
    return apiCall('/notes', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async getNoteById(noteId: number) {
    return apiCall(`/notes/${noteId}`);
  },

  async updateNote(noteId: number, data: {
    title?: string;
    content?: string;
    visibility?: 'PUBLIC' | 'PRIVATE' | 'GROUP';
    groupId?: number;
    tagIds?: number[];
  }) {
    return apiCall(`/notes/${noteId}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  async deleteNote(noteId: number) {
    return apiCall(`/notes/${noteId}`, {
      method: 'DELETE',
    });
  },

  async getAllNotes(params?: {
    page?: number;
    size?: number;
    tag?: string;
    search?: string;
  }) {
    const queryParams = new URLSearchParams();
    if (params?.page !== undefined) queryParams.append('page', params.page.toString());
    if (params?.size !== undefined) queryParams.append('size', params.size.toString());
    if (params?.tag) queryParams.append('tag', params.tag);
    if (params?.search) queryParams.append('search', params.search);

    return apiCall(`/notes?${queryParams.toString()}`);
  },

  async getUserNotes() {
    return apiCall('/notes/my-notes');
  },

  async getGroupNotes(groupId: number, page = 0, size = 20) {
    return apiCall(`/notes/group/${groupId}?page=${page}&size=${size}`);
  },

  async uploadNoteFile(noteId: number, file: File) {
    const formData = new FormData();
    formData.append('file', file);

    return apiCall(`/notes/${noteId}/upload`, {
      method: 'POST',
      body: formData,
    });
  },
};