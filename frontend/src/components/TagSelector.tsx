interface TagSelectorProps {
  selectedTags: number[];
  onTagsChange: (tags: number[]) => void;
}

export const TagSelector: React.FC<TagSelectorProps> = ({ 
  selectedTags, 
  onTagsChange 
}) => {
  const { token } = useAuth();
  const [availableTags, setAvailableTags] = useState([]);

  useEffect(() => {
    const fetchTags = async () => {
      const response = await tagService.getAllTags(token!);
      setAvailableTags(response.data);
    };
    
    if (token) fetchTags();
  }, [token]);

  const toggleTag = (tagId: number) => {
    if (selectedTags.includes(tagId)) {
      onTagsChange(selectedTags.filter(id => id !== tagId));
    } else {
      onTagsChange([...selectedTags, tagId]);
    }
  };

  return (
    <div className="flex flex-wrap gap-2">
      {availableTags.map((tag: any) => (
        <button
          key={tag.id}
          onClick={() => toggleTag(tag.id)}
          className={`px-3 py-1 rounded-full ${
            selectedTags.includes(tag.id)
              ? 'bg-blue-600 text-white'
              : 'bg-gray-200 text-gray-700'
          }`}
        >
          {tag.name}
        </button>
      ))}
    </div>
  );
};