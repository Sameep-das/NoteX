import React, { useState } from 'react';
import { Upload, Share2, Save, Bold, Italic, Underline, List, AlignLeft, AlignCenter, AlignRight, Type, Palette, Image, Link, MoreHorizontal } from 'lucide-react';
import { useRef } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { noteService } from '../services/noteService';

const NoteEditor: React.FC = () => {
  // const [noteContent, setNoteContent] = useState('');
  const [isShareOpen, setIsShareOpen] = useState(false);
  const [activeTools, setActiveTools] = useState<string[]>([]);

  const toggleTool = (tool: string) => {
    setActiveTools(prev => 
      prev.includes(tool) 
        ? prev.filter(t => t !== tool)
        : [...prev, tool]
    );
  };

  const formatText = (command: string) => {
    document.execCommand(command, false);
  };

  const [groupId, setGroupId] = useState<number | undefined>(undefined);
  const [tagIds, setTagIds] = useState<number[]>([]);
  const { token, user } = useAuth();
  const [noteContent, setNoteContent] = useState('');
  const [title, setTitle] = useState('Untitled Note');
  const [selectedTags, setSelectedTags] = useState<number[]>([]);
  // const [visibility, setVisibility] = useState('PRIVATE');
  const [visibility, setVisibility] = useState<"PRIVATE" | "PUBLIC" | "GROUP">("PRIVATE");
  const editorRef = useRef<HTMLDivElement>(null);

  const handleSave = async () => {
    if (!token) return;

    try {
      const response = await noteService.createNote({
        title,
        content: noteContent,
        noteType: 'TEXT',
        visibility,
        tagIds: selectedTags,
      });

      console.log('Note saved:', response);
    } catch (error) {
      console.error('Failed to save note:', error);
    }
  };

  const handleFileUpload = async (file: File) => {
  if (!token) return;

  try {
    // First create the note
    const noteResponse = await noteService.createNote({
      title,
      content: noteContent,
      noteType: 'IMAGE',
      visibility,
      groupId,
      tagIds
    });

    // Then upload the file
    await noteService.uploadNoteFile(noteResponse.id, file);

    console.log("Image uploaded successfully");
  } catch (error) {
    console.error('Failed to upload file:', error);
  }
};


  return (
    <div className="flex-1 flex flex-col bg-white dark:bg-gray-800 border-r border-gray-200 dark:border-gray-700 transition-colors duration-200">
      {/* Header */}
      <div className="p-6 border-b border-gray-200 dark:border-gray-700">
        <div className="flex items-center justify-between mb-4">
          <div className="flex items-center space-x-2">
            <div className="w-10 h-10 bg-gradient-to-br from-blue-500 to-purple-600 rounded-full flex items-center justify-center">
              <span className="text-white font-semibold">S</span>
            </div>
            <h1 className="text-xl font-bold text-gray-900 dark:text-white">
              Sameep <span className="text-gray-500 dark:text-gray-400">•</span> Fun-Group
            </h1>
          </div>
          <div className="flex items-center space-x-2">
            <div className="px-3 py-1 bg-green-100 dark:bg-green-900/30 text-green-700 dark:text-green-300 rounded-full text-sm font-medium">
              Auto-saved
            </div>
          </div>
        </div>

        {/* Formatting Toolbar */}
        <div className="flex items-center space-x-1 p-2 bg-gray-50 dark:bg-gray-700 rounded-lg">
          <div className="flex items-center space-x-1 border-r border-gray-200 dark:border-gray-600 pr-3">
            <ToolButton 
              icon={Bold} 
              onClick={() => formatText('bold')} 
              active={activeTools.includes('bold')}
              tooltip="Bold"
            />
            <ToolButton 
              icon={Italic} 
              onClick={() => formatText('italic')} 
              active={activeTools.includes('italic')}
              tooltip="Italic"
            />
            <ToolButton 
              icon={Underline} 
              onClick={() => formatText('underline')} 
              active={activeTools.includes('underline')}
              tooltip="Underline"
            />
          </div>
          
          <div className="flex items-center space-x-1 border-r border-gray-200 dark:border-gray-600 pr-3">
            <ToolButton 
              icon={AlignLeft} 
              onClick={() => formatText('justifyLeft')} 
              tooltip="Align Left"
            />
            <ToolButton 
              icon={AlignCenter} 
              onClick={() => formatText('justifyCenter')} 
              tooltip="Align Center"
            />
            <ToolButton 
              icon={AlignRight} 
              onClick={() => formatText('justifyRight')} 
              tooltip="Align Right"
            />
          </div>

          <div className="flex items-center space-x-1 border-r border-gray-200 dark:border-gray-600 pr-3">
            <ToolButton 
              icon={List} 
              onClick={() => formatText('insertUnorderedList')} 
              tooltip="Bullet List"
            />
            <ToolButton 
              icon={Link} 
              onClick={() => {
                const url = prompt('Enter URL:');
                if (url) formatText('createLink');
              }} 
              tooltip="Insert Link"
            />
            <ToolButton 
              icon={Image} 
              onClick={() => document.getElementById('image-upload')?.click()} 
              tooltip="Insert Image"
            />
          </div>

          <div className="flex items-center space-x-1">
            <ToolButton 
              icon={Type} 
              onClick={() => {}} 
              tooltip="Font Size"
            />
            <ToolButton 
              icon={Palette} 
              onClick={() => {}} 
              tooltip="Text Color"
            />
            <ToolButton 
              icon={MoreHorizontal} 
              onClick={() => {}} 
              tooltip="More Options"
            />
          </div>
        </div>
      </div>

      {/* Note Content Area */}
      <div className="flex-1 p-6">
        <div className="h-full border border-gray-200 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-900 focus-within:ring-2 focus-within:ring-blue-500 focus-within:border-transparent transition-all duration-200">
          <div
            contentEditable
            className="w-full h-full p-6 outline-none text-gray-900 dark:text-white resize-none leading-relaxed"
            style={{ minHeight: '400px' }}
            // placeholder="Start writing your note..."
            onInput={(e) => setNoteContent((e.target as HTMLDivElement).innerText)}
            suppressContentEditableWarning={true}
          />
        </div>
        <input 
          type="file" 
          id="image-upload" 
          className="hidden" 
          accept="image/*"
          onChange={(e) => {
            const file = e.target.files?.[0];
            if (file) {
              // Handle image upload
              console.log('Image selected:', file);
            }
          }}
        />
      </div>

      {/* Action Buttons */}
      <div className="p-6 border-t border-gray-200 dark:border-gray-700">
        <div className="flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <button className="flex items-center space-x-2 px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors duration-200 font-medium">
              <Upload className="h-4 w-4" />
              <span>Upload File</span>
            </button>
            <input type="file" className="hidden" id="file-upload" />
          </div>

          <div className="flex items-center space-x-3">
            <button className="px-4 py-2 border border-gray-300 dark:border-gray-600 text-gray-700 dark:text-gray-300 hover:bg-gray-50 dark:hover:bg-gray-700 rounded-lg transition-colors duration-200 font-medium">
              Save as Draft
            </button>
            
            <div className="relative">
              <button 
                onClick={() => setIsShareOpen(!isShareOpen)}
                className="flex items-center space-x-2 px-4 py-2 bg-green-600 hover:bg-green-700 text-white rounded-lg transition-colors duration-200 font-medium"
              >
                <Share2 className="h-4 w-4" />
                <span>Share</span>
              </button>
              
              {isShareOpen && (
                <div className="absolute right-0 top-full mt-2 w-48 bg-white dark:bg-gray-800 border border-gray-200 dark:border-gray-600 rounded-lg shadow-lg z-10 animate-slide-up">
                  <div className="p-2">
                    <button className="w-full text-left px-3 py-2 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-md transition-colors duration-200">
                      Share to Group
                    </button>
                    <button className="w-full text-left px-3 py-2 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-md transition-colors duration-200">
                      Make Public
                    </button>
                    <button className="w-full text-left px-3 py-2 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-md transition-colors duration-200">
                      Share with Co-Learners
                    </button>
                    <button className="w-full text-left px-3 py-2 text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-md transition-colors duration-200">
                      Copy Link
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

interface ToolButtonProps {
  icon: React.ComponentType<any>;
  onClick: () => void;
  active?: boolean;
  tooltip: string;
}

const ToolButton: React.FC<ToolButtonProps> = ({ icon: Icon, onClick, active = false, tooltip }) => {
  return (
    <button
      onClick={onClick}
      className={`p-2 rounded-md transition-colors duration-200 relative group ${
        active
          ? 'bg-blue-100 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400'
          : 'hover:bg-gray-200 dark:hover:bg-gray-600 text-gray-600 dark:text-gray-400'
      }`}
      title={tooltip}
    >
      <Icon className="h-4 w-4" />
      <div className="absolute -top-8 left-1/2 transform -translate-x-1/2 bg-black text-white text-xs px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition-opacity duration-200 pointer-events-none whitespace-nowrap">
        {tooltip}
      </div>
    </button>
  );
};

export default NoteEditor;